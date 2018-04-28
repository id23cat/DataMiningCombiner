package evm.dmc.web.controllers.project;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.AccountNotFoundException;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(ProjectController.BASE_URL)		// /project
@SessionAttributes({ProjectController.SESSION_Account, ProjectController.SESSION_CurrentProject})
@Scope("session")
@Slf4j
public class ProjectController {
	public static final String BASE_URL = "/project";
	
	public static final String URL_PART_DELPROJ =  "/delete";
	public static final String URL_PART_ADDPROJ = "/add";
	
	public static final String URL_GetPorject = BASE_URL + ProjectController.PATH_ProjectName;
	public static final String URL_DeleteProject = BASE_URL + URL_PART_DELPROJ;
	public static final String URL_AddProject = BASE_URL + URL_PART_ADDPROJ;
	public static final String SESSION_Account = "account";
	public static final String SESSION_CurrentProject = "currentProject";
	
	public static final String MODEL_BackBean = "backBean";
	
	public final static String MODEL_HasHeader = "hasHeader";
	public static final String MODEL_NewAlgorithm = "newAlgorithm";
	public static final String MODEL_NewProject = "newProject";
	public static final String MODEL_ProjectsSet = "projectsSet";
	
	public final static String PATH_VAR_ProjectName = "projectName";
	public final static String PATH_ProjectName = "/{" + PATH_VAR_ProjectName + "}";
	
	
	@Autowired
	private DatasetModelAppender datasetModelAppender;
	
//	@Autowired
//	private AlgorithmController algorithmController;
	@Autowired
	private AlgorithmModelAppender algoritmModelAppender;
	
//	@Autowired
	private AccountService accountService;
	
//	@Autowired
	private ProjectService projectService;
	
//	@Autowired
	private Views views;
	
	public ProjectController(@Autowired AccountService accService, 
			@Autowired ProjectService prjService, 
			@Autowired Views views) {
		this.accountService = accService;
		this.projectService = prjService;
		this.views = views;
		
	}
	
	@ModelAttribute(SESSION_Account)
	public Account getAccount(Authentication authentication) throws AccountNotFoundException {
			return accountService.getAccountByName(authentication.getName());
	}
	
	@ModelAttribute(MODEL_BackBean)
	public CheckboxNamesBean backingBeanForCheckboxes() {
		return new CheckboxNamesBean();
	}
	
	@GetMapping
	public String getAllProjects(@ModelAttribute(SESSION_Account) Account account,
			Model model, HttpSession session, SessionStatus status) {
////		status.setComplete();
//        session.removeAttribute(SESSION_CurrentProject);
//        model.addAttribute(SESSION_CurrentProject, null);
		
		List<ProjectModel> projectsSet = projectService.getByAccountAsList(account);
		projectsSet.sort(Comparator.comparing(ProjectModel::getName, String.CASE_INSENSITIVE_ORDER));
		
		model.addAttribute(MODEL_ProjectsSet, projectsSet);
		model.addAttribute(MODEL_NewProject, projectService.getNew());
		return views.getUserHome();
	}
	
	/**
	 * @param projectName
	 * @param model
	 * @return
	 * @throws ProjectNotFoundException
	 * 
	 * Open selected or newly created project
	 */
//	@GetMapping(ProjectController.PATH_ProjectName)
	@GetMapping(value=PATH_ProjectName)
	public String getProject(@PathVariable(PATH_VAR_ProjectName) String projectName,
							@ModelAttribute(SESSION_Account) Account account,
							Model model, 
							HttpServletRequest request) 
									throws ProjectNotFoundException {
		log.debug("Request for project {}", projectName);
		Optional<ProjectModel> optProject = projectService.getByNameAndAccount(projectName, account);
		if(!optProject.isPresent()){
			log.warn("Reqiest for non-existing project {}", projectName);
			return views.getErrors().getNotFound();
		}
		
		ProjectModel project = optProject.get();
		
		log.debug("Current project: {}", project.getId()+project.getName());
		
//		Set<Algorithm> algSet = project.getAlgorithms();
		
		
		model.addAttribute(SESSION_CurrentProject, project);
		
		model = algoritmModelAppender.addAttributesToModel(model, project);
		
		model = datasetModelAppender.addAttributesToModel(model, project);
			
		return views.getProject().getMain();
		
	}
	
	@PostMapping(URL_PART_ADDPROJ)
	public RedirectView postAddProject(@ModelAttribute(SESSION_Account) Account account, 
						@Valid @ModelAttribute(MODEL_NewProject) ProjectModel project,
						BindingResult bindingResult, RedirectAttributes ra) {
		if(bindingResult.hasErrors()) {
			log.debug("Invalid new project's property: {}", bindingResult);
			
			new RedirectView(RequestPath.project);
		}
		log.debug("Registering new project {}", project.getName());

		accountService.addProject(account, project);
		
		return new RedirectView(RequestPath.project + "/" + project.getName());
	}
	
	@PostMapping(URL_PART_DELPROJ)
	public RedirectView postDelProject(
			@ModelAttribute(SESSION_Account) Account account,
			@ModelAttribute(MODEL_BackBean) CheckboxNamesBean bean,
			BindingResult bindingResult, RedirectAttributes ra
			) {
//		log.debug("BackBean: {}", Arrays.stream(bean.getNames()).collect(Collectors.toList()));
		log.debug("BackBean: {}", bean.getNamesSet());
		
//		projectService.deleteByAccountAndNames(account, new HashSet<String>(Arrays.asList(bean.getNames())));
		projectService.deleteByAccountAndNames(account, bean.getNamesSet());
		
		log.debug("==============redirect===================");
		return new RedirectView(ProjectController.BASE_URL);
	}
	
}
