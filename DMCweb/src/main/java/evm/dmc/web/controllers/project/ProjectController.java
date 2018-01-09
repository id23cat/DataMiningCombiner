package evm.dmc.web.controllers.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.controllers.CheckboxBean;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.impls.AccountService;
import lombok.Data;
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
		model.addAttribute("projectsSet", account.getProjects());
		model.addAttribute("newProject", projectService.getNew());
		model.addAttribute("backBean", new CheckboxBean());
		
		return views.getProject().getMain();
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
			
			return views.getProject().getMain();
		}
		log.debug("Registering new project {}", project.getName());

		account.addProject(project);
		accountService.save(account);
		return "redirect:" + RequestPath.project + "/" + project.getName();
	}
	
	@PostMapping(RequestPath.delete)
	public String postDelProjedct(
			@ModelAttribute("account") Account account,
			@ModelAttribute("backBean") CheckboxBean bean,
			BindingResult bindingResult, RedirectAttributes ra
			) {
		log.debug("BackBean: {}", Arrays.stream(bean.getNames()).collect(Collectors.toList()));
		
		account.getProjects().removeIf((proj) -> nameContainsOneOf(proj.getName(), bean.getNames()));
		accountService.save(account);
		return "redirect:" + RequestPath.project;
	}
	
	private static boolean nameContainsOneOf(String name, String[] names) {
		return Arrays.stream(names).anyMatch(name :: contains);
	}

}
