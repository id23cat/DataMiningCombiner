package evm.dmc.web.controllers.project;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.AccountService;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(ProjectController.BASE_URL)		// /project
@SessionAttributes({ProjectController.SESSION_Account, ProjectController.SESSION_CurrentProject})
@Slf4j
public class ProjectController {
	public static final String BASE_URL = RequestPath.project;
	public static final String SESSION_Account = "account";
	public static final String SESSION_CurrentProject = "currentProject";
	
	public static final String MODEL_AlgBasePath = "algBasePath";
	public static final String MODEL_AlgorithmsSet = "algorithmsSet";
	public static final String MODEL_BackBean = "backBean";
	public static final String MODEL_DataSet = "dataSet";
	public static final String MODEL_NewAlgorithm = "newAlgorithm";
	public static final String MODEL_NewProject = "newProject";
	public static final String MODEL_ProjectsSet = "projectsSet";
	
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
	public Account getAccount(Authentication authentication) throws UserNotExistsException {
			return accountService.getAccountByName(authentication.getName());
	}
	
	@ModelAttribute(MODEL_BackBean)
	public CheckboxNamesBean backingBeanForCheckboxes() {
		return new CheckboxNamesBean();
	}
	
	@GetMapping
	public String getAllProjects(@ModelAttribute(SESSION_Account) Account account, Model model) {
		
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
	@GetMapping(value="{projectName}")
	public String getProject(@PathVariable String projectName,
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
		
		Set<Algorithm> algSet = project.getAlgorithms();
		Set<MetaData> dataSet = project.getDataSources();
		
		model.addAttribute(SESSION_CurrentProject, project);
		model.addAttribute(MODEL_AlgorithmsSet, algSet);
		model.addAttribute(MODEL_DataSet, dataSet);
		model.addAttribute(MODEL_AlgBasePath, request.getServletPath() + RequestPath.algorithm);
		log.debug("algBasePath : {}", request.getServletPath() + RequestPath.algorithm);
		model.addAttribute(MODEL_NewAlgorithm, projectService.getNewAlgorithm());
		return views.getProject().getMain();
		
	}
	
	@PostMapping(RequestPath.add)
	public RedirectView postAddProject(@ModelAttribute(SESSION_Account) Account account, 
						@Valid @ModelAttribute(MODEL_NewProject) ProjectModel project,
						BindingResult bindingResult, RedirectAttributes ra) {
		if(bindingResult.hasErrors()) {
			log.debug("Invalid new project property: {}", bindingResult);
			
			new RedirectView(RequestPath.project);
		}
		log.debug("Registering new project {}", project.getName());

		accountService.addProject(account, project);
		
		return new RedirectView(RequestPath.project + "/" + project.getName());
	}
	
	@PostMapping(RequestPath.delete)
	public RedirectView postDelProjedct(
			@ModelAttribute(SESSION_Account) Account account,
			@ModelAttribute(MODEL_BackBean) CheckboxNamesBean bean,
			BindingResult bindingResult, RedirectAttributes ra
			) {
		log.debug("BackBean: {}", Arrays.stream(bean.getNames()).collect(Collectors.toList()));
		
		projectService.deleteByAccountAndNames(account, new HashSet<String>(Arrays.asList(bean.getNames())));
		
		log.debug("==============redirect===================");
		return new RedirectView(RequestPath.project);
	}
	
	
	/**
	 * @param project
	 * @param algorithm
	 * @return
	 * 
	 * Handles request to /project/algorithm/add
	 */
	@PostMapping(RequestPath.assignAlgorithm)
	public RedirectView postAssignAlgorithm(
			@SessionAttribute(SESSION_CurrentProject) ProjectModel project,
			@Valid @ModelAttribute(MODEL_NewAlgorithm) Algorithm algorithm
			) {
		log.debug("Adding algorithm: {}" ,algorithm.getName());
		log.debug("Project: {}", project.getId() + project.getName());
		
		projectService.assignAlgorithm(project, algorithm);

		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
	@PostMapping(RequestPath.delAlgorithm)
	public RedirectView postDelAlgorithm(
			@SessionAttribute(SESSION_CurrentProject) ProjectModel project,
			@ModelAttribute(MODEL_BackBean) CheckboxNamesBean bean
			) {
		log.debug("Selected algorithms for deleteion:{}", StringUtils.arrayToCommaDelimitedString(bean.getNames()));
		log.debug("Project for deletion in: {}", project);
		
		projectService.deleteAlgorithms(project, new HashSet<String>(Arrays.asList(bean.getNames())));
		
		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
}
