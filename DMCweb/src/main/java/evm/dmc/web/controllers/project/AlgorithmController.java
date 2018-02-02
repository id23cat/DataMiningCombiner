package evm.dmc.web.controllers.project;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import evm.dmc.web.service.data.DataPreview.HeaderItem;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}")
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class AlgorithmController {
	public final static String BASE_URL = RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}";
	public final static String SESSION_Account = "account";
	public final static String SESSION_CurrProject = "currentProject";
	
	public final static String MODEL_PreviewData = "previewData";
	public final static String MODEL_Algorithm = "algorithm";
	public final static String MODEL_PagesMap = "pagesMap";
	public final static String MODEL_HeaderItems = "headerItems";
	public final static String MODEL_SrcAttrURI = "sourceAttributesURI";
	public final static String MODEL_SrcUploadURI = "sourceUploadURI";
	public final static String MODEL_PostFile = "file";
	
	public final static String PATH_AlgName = "algName";
	public final static String PATH_ProjectName = "projectName";
	
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private Views views;
	
	@ModelAttribute(SESSION_Account)
	public Account getAccount(Authentication authentication) throws UserNotExistsException {
			log.debug("Call to create account session bean");
			return accountService.getAccountByName(authentication.getName());
	}
	
	@ModelAttribute(SESSION_CurrProject)
	public ProjectModel getCurrentProjectInSession(
			@PathVariable(PATH_ProjectName) String projectName,
			@ModelAttribute(SESSION_Account) Account account) throws ProjectNotFoundException {
		log.debug("Call to create currentProject session bean");
		Optional<ProjectModel> optProject = projectService.getByNameAndAccount(projectName, account);
		if(!optProject.isPresent()){
			log.warn("Reqiest for non-existing project {}", projectName);
			throw new ProjectNotFoundException(String.format("Project with name %s owned by user %s not found", projectName, account.getUserName()));
		}
		
		log.trace("Selected project: {}", optProject.get());
		return optProject.get();
		
	}
	
//	@ModelAttribute("headerItems")
//	public DataPreview.ItemsList modelDataPreviewArray(@ModelAttribute(name="previewData") Optional<DataPreview> preview) {
//		if(preview.isPresent()){
//			log.trace("-== PreviewData is present");
//			return new DataPreview.ItemsList(preview.get().getHeaderItems());
//		}
//		return null;// new DataPreview.ItemsList();
//	}
	
	
	@GetMapping
	public String getAlgorithm(
			@PathVariable(PATH_ProjectName) String projectName,
			@PathVariable(PATH_AlgName) String algName,
			@ModelAttribute(SESSION_CurrProject) ProjectModel project,
			@ModelAttribute(MODEL_PreviewData) Optional<DataPreview> preview, 
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
			
			
//			model.addAttribute(MODEL_Algorithm, algorithm);
//			model.addAttribute(MODEL_PagesMap, new LinkedHashMap<String,String>());
//			return "redirect:" + RequestPath.algorithmWozard;
			UriComponents srcUploadUri = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
					.buildAndExpand(project.getName(), algName);
			model.addAttribute(MODEL_SrcUploadURI, srcUploadUri.toUriString());
			
			UriComponents srcAttribUri = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSourceAttributes)
					.buildAndExpand(project.getName(), algName);
			model.addAttribute(MODEL_SrcAttrURI, srcAttribUri.toUriString());
			
			if(preview.isPresent()) {
				model.addAttribute(MODEL_PreviewData, preview.get());
				model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList(preview.get().getHeaderItems()));
			}
			
			{ // for debug reasons only
				log.warn("-== Debugging section: is needed to remove");
				DataPreview prev = fileService.loadDataPreview(Paths.get("idcat","proj0"), "telecom_churn.csv");
				model.addAttribute(MODEL_PreviewData, prev);
//				model.addAttribute("headerItems", new DataPreview.ItemsList(prev.getHeaderItems()));
				model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList());
				
			}
			
			return views.project.wizard.datasource;
		}
//		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
//				.buildAndExpand(project.getName(), algName);
//		model.addAttribute("sourceUpload", uriComponents.toUriString());
//		log.debug("POST source URI: {}", uriComponents.toUriString());
		
		return "project/algorithm/datasource";
	}
	
	@PostMapping(RequestPath.setSource)
	public RedirectView postSourceFile(@RequestParam(MODEL_PostFile) MultipartFile file,
			@SessionAttribute(SESSION_Account) Account account,
			@SessionAttribute(SESSION_CurrProject) ProjectModel project,
			@PathVariable(PATH_AlgName) String algName,
			RedirectAttributes ra) {
		
		DataPreview preview = fileService.store(FileStorageService.relativePath(account, project), file);
		ra.addFlashAttribute(MODEL_PreviewData, preview);
		
		log.debug("-== Receiving file: {}", file.getName());
		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
										.buildAndExpand(project.getName(), algName);
		
		log.debug("-== Saving complete");
		return new RedirectView(uriComponents.toUriString());
	}
	
	@PostMapping(RequestPath.setSourceAttributes)
	public RedirectView postSourceAttribytes(
			@Valid @ModelAttribute(MODEL_HeaderItems) DataPreview.ItemsList attributes, 
			@PathVariable(PATH_ProjectName) String projName,
			@PathVariable(PATH_AlgName) String algName) {
		
		List<HeaderItem> selectedItems = attributes.getItems().stream()
												.filter((item) -> {return item.isChecked();})
												.collect(Collectors.toList());
		log.debug("-== Getting data attributes comlete");
		log.trace("SelectedItems: {}", selectedItems.size());
		log.trace("Items: {}", selectedItems);
		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
				.buildAndExpand(projName, algName);
		return new RedirectView(uriComponents.toUriString());
	}

}
