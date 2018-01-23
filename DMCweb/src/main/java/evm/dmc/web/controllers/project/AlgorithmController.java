package evm.dmc.web.controllers.project;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import evm.dmc.web.service.FileStorageService;
import evm.dmc.web.service.RequestPath;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}")
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class AlgorithmController {
	private final static String BASE_URL = RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}";
	
	@Autowired
	private FileStorageService fileService;
	
	@GetMapping
	public String getAlgorithm(
			@PathVariable("projectName") String projectName,
			@PathVariable("algName") String algName,
			@SessionAttribute("currentProject") ProjectModel project,
			Model model,
			HttpServletRequest request) throws AlgorithmNotFoundException {
		Optional<AlgorithmModel> algorithm = project.getAlgorithms()
										.stream()
										.filter((alg) -> alg.getName().equals(algName))
										.findAny();
		
		if(algorithm.orElseThrow(AlgorithmNotFoundException::new).getFunctions().isEmpty()) {
			log.debug("-== Emty algorithm selected. Running wizard: {}", RequestPath.algorithmWozard);
			return "redirect:" + RequestPath.algorithmWozard;
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
		fileService.store(FileStorageService.relativePath(account, project), file);
		
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
										.buildAndExpand(project.getName(), algName);
		return new RedirectView(uriComponents.toUriString());
	}

}
