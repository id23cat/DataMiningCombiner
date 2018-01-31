package evm.dmc.web.controllers.project;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.FileStorageService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.data.DataPreview;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}")
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class AlgorithmController {
	public final static String BASE_URL = RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}";
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private Views views;
	
	@ModelAttribute("account")
	public Account getAccount(Authentication authentication) throws UserNotExistsException {
			log.debug("Call to create account session bean");
			return accountService.getAccountByName(authentication.getName());
	}
	
	@ModelAttribute("currentProject")
	public ProjectModel getCurrentProjectInSession(
			@PathVariable("projectName") String projectName,
			@ModelAttribute("account") Account account) throws ProjectNotFoundException {
		log.debug("Call to create currentProject session bean");
		Optional<ProjectModel> optProject = projectService.getByNameAndAccount(projectName, account);
		if(!optProject.isPresent()){
			log.warn("Reqiest for non-existing project {}", projectName);
			throw new ProjectNotFoundException(String.format("Project with name %s owned by user %s not found", projectName, account.getUserName()));
		}
		
		log.trace("Selected project: {}", optProject.get());
		return optProject.get();
		
	}
	
	
	@GetMapping
	public String getAlgorithm(
			@PathVariable("projectName") String projectName,
			@PathVariable("algName") String algName,
			@ModelAttribute("currentProject") ProjectModel project,
			@ModelAttribute(name="previewData") Optional<DataPreview> preview, 
			Model model,
			HttpServletRequest request) throws AlgorithmNotFoundException {
		AlgorithmModel algorithm = project.getAlgorithms()
										.stream()
										.filter((alg) -> alg.getName().equals(algName))
										.findAny()
										.orElseThrow(AlgorithmNotFoundException::new);
		
		// if algorithm is empty (new algorithm)
		if(algorithm.getFunctions().isEmpty()) {
			log.debug("-== Emty algorithm selected. Running wizard: {}", RequestPath.algorithmWozard);
			
			
			model.addAttribute("algorithm", algorithm);
			model.addAttribute("pagesMap", new LinkedHashMap<String,String>());
			
//			return "redirect:" + RequestPath.algorithmWozard;
			UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
					.buildAndExpand(project.getName(), algName);
			model.addAttribute("sourceUpload", uriComponents.toUriString());
			
//			{ // for debug reasons only
//				log.warn("-== Debugging section: is needed to remove");
//				model.addAttribute("previewData", fileService.loadDataPreview(Paths.get("idcat","proj0"), "telecom_churn.csv"));
//				
//			}
			
			return views.project.wizard.datasource;
		}
//		else {
//			return String.format("redirect:" + RequestPath.project + "/" + projectName);
//		}
		
//		model.addAttribute("sourceUpload", String.format("%s/%s", request.getServletPath(), RequestPath.setSource));
		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
				.buildAndExpand(project.getName(), algName);
		model.addAttribute("sourceUpload", uriComponents.toUriString());
		log.debug("POST source URI: {}", uriComponents.toUriString());
		
		return "project/algorithm/datasource";
	}
	
	@PostMapping(RequestPath.setSource)
	public RedirectView postSourceFile(@RequestParam("file") MultipartFile file,
			@SessionAttribute("account") Account account,
			@SessionAttribute("currentProject") ProjectModel project,
			@PathVariable("algName") String algName,
			RedirectAttributes ra) {
		DataPreview preview = fileService.store(FileStorageService.relativePath(account, project), file);
		ra.addFlashAttribute("previewData", preview);
		
		log.debug("-== Receiving file: {}", file.getName());
		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
										.buildAndExpand(project.getName(), algName);
		
		log.debug("-== Saving complete");
		return new RedirectView(uriComponents.toUriString());
	}

}
