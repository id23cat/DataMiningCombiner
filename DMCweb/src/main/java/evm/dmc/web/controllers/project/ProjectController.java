package evm.dmc.web.controllers.project;

import javax.validation.Valid;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping
	public String getProjectsList(@ModelAttribute("account") Account account, Model model) {
		model.addAttribute("projectsList", account.getProjects());
		model.addAttribute("newProject", projectService.getNew());
		log.debug("Projects: {}", account.getProjects().toString());
		return views.getProject().getMain();
	}
	
	@GetMapping(value="{projectName}")
	public String getProject(@PathVariable String projectName, Model model) throws ProjectNotFoundException{
		String sessionProject = "currentProject";
		if(!model.containsAttribute(sessionProject)) {
			model.addAttribute(sessionProject, projectService.getByName(projectName));
		}
		ProjectModel project = (ProjectModel) model.asMap().get(sessionProject);
		if(project.getAlgorithms().isEmpty()){
			return views.getProject().getNewAlg();
		}else
			// TODO: not yet implemented
			return views.getProject().getAlgorithmsList();
		
	}
	
	@PostMapping(RequestPath.add)
	public String getAddProject(@ModelAttribute("account") Account account, 
						@Valid @ModelAttribute ProjectModel project, 
						Errors errors, RedirectAttributes ra) {
		log.debug("Registering new project {}", project.getProjectName());
		account.addProject(project);
		accountService.save(account);
		return "redirect:" + RequestPath.project;
	}

}
