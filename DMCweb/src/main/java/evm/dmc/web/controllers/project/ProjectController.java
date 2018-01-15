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

import javax.validation.Valid;

import org.apache.tomcat.util.buf.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
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
import evm.dmc.web.service.AccountService;
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
//		accountService.refresh(account);
//		log.debug("Avaliable projects {}", account.getProjects());
//		model.addAttribute("projectsSet", account.getProjects().stream().collect(Collectors.toList()));
		
		List<ProjectModel> projectsSet = projectService.getByAccountAsList(account);
		projectsSet.sort(Comparator.comparing(ProjectModel::getName, String.CASE_INSENSITIVE_ORDER));
		
		model.addAttribute("projectsSet", projectsSet);
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
	public String getProject(@PathVariable String projectName,
							@ModelAttribute("account") Account account,
							Model model) 
									throws ProjectNotFoundException {
		String currentProject = "currentProject";
		log.debug("Request for project {}", projectName);
		Optional<ProjectModel> optProject = projectService.getByNameAndAccount(projectName, account);
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
	@Transactional
	public String postAddProject(@ModelAttribute("account") Account account, 
						@Valid @ModelAttribute("newProject") ProjectModel project,
						BindingResult bindingResult, RedirectAttributes ra) {
		if(bindingResult.hasErrors()) {
			log.debug("Invalid new project property: {}", bindingResult);
			
			return views.getProject().getMain();
		}
		log.debug("Registering new project {}", project.getName());

		account = accountService.merge(account);
		account.addProject(project);
		accountService.save(account);
		
		return "redirect:" + RequestPath.project + "/" + project.getName();
//		return "redirect:" + RequestPath.project;
	}
	
	@PostMapping(RequestPath.delete)
	@Transactional
	public String postDelProjedct(
			@ModelAttribute("account") Account account,
			@ModelAttribute("backBean") CheckboxBean bean,
			BindingResult bindingResult, RedirectAttributes ra
			) {
		log.debug("BackBean: {}", Arrays.stream(bean.getNames()).collect(Collectors.toList()));
		
		
		projectService.deleteAllByNames(Arrays.asList(bean.getNames()));
		
		log.debug("==============redirect===================");
		return "redirect:" + RequestPath.project;
	}
	
	
}
