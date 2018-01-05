package evm.dmc.web.controllers.project;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project)
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class ProjectController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private Views views;
	
	@ModelAttribute("account")
	public Account getAccount(Authentication authentication) throws UserNotExistsException {
			return accountService.getAccountByName(authentication.getName());
	}
	
	@Transactional(readOnly = true)
	@GetMapping
	public String getProjectsList(@ModelAttribute("account") Account account, Model model) {
		Set<ProjectModel> projectsSet;
		try(Stream<ProjectModel> pros = projectService.getByAccount(account)) {
			projectsSet = pros.collect(Collectors.toSet());
		}
		account.setProjects(projectsSet);
//		model.addAttribute("projectsList", projectsSet);
		model.addAttribute("newProject", projectService.getNew());
		
		log.debug("Projects: {}", projectsSet.stream()
				.map(
						(proj)->{return String.format("{ID=%d, Name=%s}", proj.getId(), proj.getProjectName());}
					).toArray());
		for(ProjectModel pro: projectsSet){
			log.debug("From List: {}",pro.getProjectName());
		}
		
		for(ProjectModel pro: account.getProjects()) {
			log.debug("From Acclount {}", pro.getProjectName());
		}
		
		return views.getProject().getMain();
	}
	
	@GetMapping(value="{projectName}")
	public String getProject(@PathVariable String projectName, Model model) throws ProjectNotFoundException{
		String currentProject = "currentProject";
		log.debug("Request for project {}", projectName);
		Optional<ProjectModel> optProject = projectService.getByName(projectName);
		if(!optProject.isPresent()){
			log.warn("Reqiest for non-existing project {}", projectName);
			return views.getErrors().getNotFound();
		}
		
		ProjectModel project = optProject.get();
		model.addAttribute(currentProject, project);
		
		Set<AlgorithmModel> algSet = project.getAlgorithms(); 
		if(algSet.isEmpty()){
			return views.getProject().getNewAlg();
		}else
			model.addAttribute("alggorithmsSet", algSet);
			return views.getProject().getAlgorithmsList();
		
	}
	
	@PostMapping(RequestPath.add)
	public String postAddProject(@ModelAttribute("account") Account account, 
						@Valid @ModelAttribute("newProject") ProjectModel project,
						BindingResult bindingResult, RedirectAttributes ra) {
		if(bindingResult.hasErrors()) {
			log.debug("Invalid new project property: {}", bindingResult);
//			model.addAttribute("newProject", project);
			return views.getProject().getMain();
		}
		log.debug("Registering new project {}", project.getProjectName());
		project.setAccount(account);
		projectService.save(Optional.of(project));
//		account.addProject(project);
//		accountService.save(account);
		return "redirect:" + RequestPath.project + "/" + project.getProjectName();
	}
	
	@PostMapping(RequestPath.delete)
	public String postDelProjedct(@ModelAttribute Account account) {
		log.debug("Selected projects: {}", account.getProjects());
		return "redirect:" + RequestPath.project;
	}

}
