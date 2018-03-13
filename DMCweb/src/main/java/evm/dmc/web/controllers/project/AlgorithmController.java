package evm.dmc.web.controllers.project;

import java.util.Optional;

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

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(AlgorithmController.BASE_URL)	// /project/{projectName}/algorithm/{algName}
@SessionAttributes({AlgorithmController.SESSION_Account, AlgorithmController.SESSION_CurrProject})
@Slf4j
public class AlgorithmController {
	public final static String BASE_URL = RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}";
	public final static String SESSION_Account = "account";
	public final static String SESSION_CurrProject = "currentProject";
	public final static String SESSION_CurrAlgorithm = "currentAlgorithm";
	
	public final static String MODEL_MetaData = "metaData";
	public final static String MODEL_Preview = "preview";
	public final static String MODEL_Algorithm = "algorithm";
	public final static String MODEL_PagesMap = "pagesMap";
	public final static String MODEL_HasHeader = "hasHeader";
	public final static String MODEL_HeaderItems = "headerItems";
	public final static String MODEL_SrcAttrURI = "sourceAttributesURI";
	public final static String MODEL_SrcUploadURI = "sourceUploadURI";
	public final static String MODEL_PostFile = "file";
	
	public final static String PATH_AlgName = "algName";
	public final static String PATH_ProjectName = "projectName";
	
	@Autowired
	private DataStorageService dataStorageService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private AlgorithmService algorithmService;
	
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
		return projectService.getByNameAndAccount(projectName, account)
				.orElseThrow(() ->
				new ProjectNotFoundException(String.format("Project with name %s owned by user %s not found", projectName, account.getUserName())));
	}
	
	@ModelAttribute(SESSION_CurrAlgorithm)
	public Algorithm getCurrentAlgorithm(
			@ModelAttribute(SESSION_CurrProject) ProjectModel project, 
			@PathVariable(PATH_AlgName) String algName
			) throws AlgorithmNotFoundException {
		return algorithmService.getByNameAndParentProject(algName, project)
				.orElseThrow( () ->
						new AlgorithmNotFoundException(String.format("Algorithm with name %s not found", algName)));
	}
	
	@GetMapping
	public String getAlgorithm(
			@PathVariable(PATH_ProjectName) String projectName,
			@PathVariable(PATH_AlgName) String algName,
			@ModelAttribute(SESSION_CurrAlgorithm) Algorithm algorithm,
			@ModelAttribute(SESSION_CurrProject) ProjectModel project,
			@ModelAttribute(MODEL_MetaData) Optional<MetaData> metaData, 
			Model model,
			HttpServletRequest request) throws AlgorithmNotFoundException {
		
		log.debug("Getting algorithm inners");
		// if algorithm is empty (new algorithm)
		if(algorithm.isSubAlgorithm() && algorithm.getAlgorithmSteps().isEmpty()) {
			log.debug("Prepare new algorithm");
			UriComponents srcUploadUri = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
					.buildAndExpand(project.getName(), algName);
			model.addAttribute(MODEL_SrcUploadURI, srcUploadUri.toUriString());
			
			UriComponents srcAttribUri = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSourceAttributes)
					.buildAndExpand(project.getName(), algName);
			model.addAttribute(MODEL_SrcAttrURI, srcAttribUri.toUriString());
			
			HasHeaderCheckbox hasHeader = new HasHeaderCheckbox();
			
			if(metaData.isPresent()) {
				model.addAttribute(MODEL_MetaData, metaData.get());
				model.addAttribute(MODEL_Preview, dataStorageService.getPreview(metaData.get()));
				hasHeader.setHasHeader(metaData.get().getStorage().isHasHeader());
//				model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList(preview.get().getHeaderItems()));
			}
			model.addAttribute(MODEL_HasHeader, hasHeader);
//			{ // for debug reasons only
//				log.warn("-== Debugging section: is needed to remove");
//				DataPreview prev = fileService.loadDataPreview(Paths.get("idcat","proj0"), "telecom_churn.csv");
//				model.addAttribute(MODEL_PreviewData, prev);
//				model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList());
//				
//			}
			
			return views.project.wizard.datasource;
		}
//		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL).path(RequestPath.setSource)
//				.buildAndExpand(project.getName(), algName);
//		model.addAttribute("sourceUpload", uriComponents.toUriString());
//		log.debug("POST source URI: {}", uriComponents.toUriString());
		
		return "project/algorithm/datasource";
	}
	
}
