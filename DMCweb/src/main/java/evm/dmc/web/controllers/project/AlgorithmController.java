package evm.dmc.web.controllers.project;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(AlgorithmController.BASE_URL)	// /project/{projectName}/algorithm
@SessionAttributes({ProjectController.SESSION_Account, 
	ProjectController.SESSION_CurrentProject,
	AlgorithmController.SESSION_CurrentAlgorithm})
@Slf4j
public class AlgorithmController {
	public final static String URL_PART_ALGORITHM = "/algorithm";
	
	public final static String BASE_URL = ProjectController.URL_GetPorject + URL_PART_ALGORITHM;
	public final static String URL_Add_Algorithm = BASE_URL + RequestPath.add;
	
	public static final String PATH_VAR_AlgorithmName = "algName";
	public static final String PATH_AlgorithmName = "/{" + PATH_VAR_AlgorithmName + "}";
	
	public final static String SESSION_CurrentAlgorithm = "currentAlgorithm";
	
	public final static String MODEL_Algorithm = "algorithm";
	public final static String MODEL_PagesMap = "pagesMap";
	public static final String MODEL_AlgBaseURL = "algBaseURL";
	public static final String MODEL_AlgorithmsSet = "algorithmsSet";
	public static final String MODEL_NewAlgorithm = "newAlgorithm";
	
	@Autowired
	private AlgorithmService algorithmService;
	
//	@Autowired
//	private DatasetController datasetController;
	
	@Autowired
	private Views views;
	
	@Autowired
	private AlgorithmModelAppender modelAppender;
	
	@Component
	public class AlgorithmModelAppender {
		public Model addAttributesToModel(Model model, ProjectModel project) {
			Set<Algorithm> algSet = algorithmService.getForProject(project);
			
			// algorithm attributes
			model.addAttribute(MODEL_AlgorithmsSet, algSet);
			UriComponents baseUrl = UriComponentsBuilder.fromPath(BASE_URL)
					.buildAndExpand(project.getName());
			model.addAttribute(MODEL_AlgBaseURL, baseUrl.toString());
			model.addAttribute(MODEL_NewAlgorithm, AlgorithmService.getNewAlgorithm());
			
			return model;
		}
	}
	
//	@ModelAttribute(ProjectController.SESSION_Account)
//	public Account getAccount(Authentication authentication) throws UserNotExistsException {
//			log.debug("Call to create account session bean");
//			return accountService.getAccountByName(authentication.getName());
//	}
//	
//	@ModelAttribute(ProjectController.SESSION_CurrentProject)
//	public ProjectModel getCurrentProjectInSession(
//			@PathVariable(PATH_ProjectName) String projectName,
//			@ModelAttribute(ProjectController.SESSION_Account) Account account) throws ProjectNotFoundException {
//		log.debug("Call to create currentProject session bean");
//		return projectService.getByNameAndAccount(projectName, account)
//				.orElseThrow(() ->
//				new ProjectNotFoundException(String.format("Project with name %s owned by user %s not found", projectName, account.getUserName())));
//	}

	
	
//	@ModelAttribute(SESSION_CurrentAlgorithm)
//	public Algorithm getCurrentAlgorithm(
//			@ModelAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project, 
//			@PathVariable(PATH_AlgName) String algName
//			) throws AlgorithmNotFoundException {
//		return algorithmService.getByNameAndProject(algName, project)
//				.orElseThrow( () ->
//						new AlgorithmNotFoundException(String.format("Algorithm with name %s not found", algName)));
//	}
	
	@GetMapping
	public String getAlgorithmsList(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model) {
		model = modelAppender.addAttributesToModel(model, project);
		return views.project.getAlgorithmsList();
	}
	
	@GetMapping(PATH_AlgorithmName)
	public String getAlgorithm(
			@PathVariable(PATH_VAR_AlgorithmName) String algName,
			@ModelAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model,
			HttpServletRequest request) throws AlgorithmNotFoundException {
		
		log.trace("Getting algorithm");
		Optional<Algorithm> optAlgorithm = algorithmService.getByProjectAndName(project, algName);
		if(!optAlgorithm.isPresent()){
			log.warn("Reqiest for non-existing project {}", algName);
			return views.getErrors().getNotFound();
		}
		Algorithm algorithm = optAlgorithm.get();
		// if algorithm is empty (new algorithm)
//		if(algorithm.getMethod()..getAlgorithmSteps().isEmpty()) {
//			log.debug("Prepare new algorithm");
//
//			model = datasetController.addAttributesToModel(model, project, metaData);
//			
//			return views.project.wizard.datasource;
//		}
		return views.project.algorithm.algorithm;
	}
	
	/**
	 * @param project
	 * @param algorithm
	 * @return
	 * 
	 * Handles request to /project/algorithm/add
	 */
	@PostMapping(RequestPath.add)
	public RedirectView postAddAlgorithm(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@Valid @ModelAttribute(MODEL_NewAlgorithm) Algorithm algorithm, 
			BindingResult bindingResult
			) {
		if(bindingResult.hasErrors()) {
			log.debug("Invalid new algorithm's property: {}", bindingResult);
			
			new RedirectView(BASE_URL);
		}
		
		log.debug("Adding algorithm: {}" ,algorithm.getName());
		log.debug("to project: {}", project.getId() + project.getName());
		
		algorithm = algorithmService.addNew(project, algorithm);

		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
	@PostMapping(RequestPath.delete)
	public RedirectView postDelAlgorithm(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@ModelAttribute(ProjectController.MODEL_BackBean) CheckboxNamesBean bean
			) {
//		log.debug("Selected algorithms for deleteion:{}", StringUtils.arrayToCommaDelimitedString(bean.getNames()));
		log.debug("Selected algorithms for deleteion:{}", bean.getNamesSet());
		log.debug("Project for deletion in: {}", project);
		
//		projectService.deleteAlgorithms(project, new HashSet<String>(Arrays.asList(bean.getNames())));
		algorithmService.delete(project, bean.getNamesSet());
		
		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
}
