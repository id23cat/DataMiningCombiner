package evm.dmc.web.controllers.project;

import java.util.Optional;
import java.util.Set;

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
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(DatasetController.BASE_URL)
@SessionAttributes({ProjectController.SESSION_Account, 
	ProjectController.SESSION_CurrentProject,
	AlgorithmController.SESSION_CurrentAlgorithm})
@Slf4j
public class DatasetController {
	public static final String BASE_URL=RequestPath.project + DatasetController.PATH_ProjectName + RequestPath.dataset;
	public static final String URL_SetSource = BASE_URL+RequestPath.setSource;
	public static final String URL_SetAttributes = BASE_URL+RequestPath.setSourceAttributes;
	
	public static final String MODEL_DataSet = "dataSet";
	public final static String MODEL_PostFile = "file";
	public final static String MODEL_MetaData = "metaData";
	public final static String MODEL_DataUploadURL = "dataUploadURL";
	public final static String MODEL_DataAttributesURL = "dataAttributesURL";
	public final static String MODEL_HasHeader = "hasHeader";
	public final static String MODEL_Preview = "preview";
	
	public final static String PATH_ProjectName = "/{projectName}";

//	@Autowired
	private DataStorageService dataStorageService;
	
//	@Autowired
	private MetaDataService metaDataService;
	
//	@Autowired
	private Views views;
	
	
	
	public DatasetController(@Autowired DataStorageService dataStorageService,
			@Autowired MetaDataService metaDataService, 
			@Autowired Views views) {
		super();
		this.dataStorageService = dataStorageService;
		this.metaDataService = metaDataService;
		this.views = views;
	}

	public Model addAttributesToModel(Model model, ProjectModel project) {
		Set<MetaData> dataSet = metaDataService.getForProject(project);
		
		model.addAttribute(MODEL_DataSet, dataSet);
		UriComponents srcUploadUri = UriComponentsBuilder.fromPath(URL_SetSource)
				.buildAndExpand(project.getName());
		model.addAttribute(MODEL_DataUploadURL, srcUploadUri.toUriString());
		
		UriComponents srcAttrdUri = UriComponentsBuilder.fromPath(URL_SetAttributes)
				.buildAndExpand(project.getName());
		model.addAttribute(MODEL_DataAttributesURL, srcAttrdUri.toUriString());
		
		HasHeaderCheckbox hasHeader = new HasHeaderCheckbox();
		model.addAttribute(MODEL_HasHeader, hasHeader);
		
		return model;
	}
	
	public Model addAttributesToModel(Model model, ProjectModel project, Optional<MetaData> metaData) {
		model = addAttributesToModel(model, project);
		if(metaData.isPresent()) {
			model.addAttribute(MODEL_MetaData, metaData.get());
			model.addAttribute(MODEL_Preview, dataStorageService.getPreview(metaData.get()));
			HasHeaderCheckbox hasHeader = (HasHeaderCheckbox) model.asMap().get(MODEL_HasHeader);
			hasHeader.setHasHeader(metaData.get().getStorage().isHasHeader());
//			model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList(preview.get().getHeaderItems()));
		}
		return model;
	}
	
//	@ModelAttribute(ProjectController.SESSION_Account)
//	public Account getAccount(Authentication authentication) throws UserNotExistsException {
//			return accountService.getAccountByName(authentication.getName());
//	}
//	
//	@ModelAttribute(ProjectController.SESSION_CurrentProject)
//	public ProjectModel getCurrentProjectInSession(
//			@PathVariable(PATH_ProjectName) String projectName,
//			@ModelAttribute(ProjectController.SESSION_Account) Account account) throws ProjectNotFoundException {
//		log.debug("Call to create currentProject session bean");
//		return projectService.getByNameAndAccount(projectName, account)
//				.orElseThrow(() ->
//				new ProjectNotFoundException(String.format("Project with name %s owned by user %s not found", projectName, account.getUserName())));
//	}
	
	@GetMapping
	public String getDataSetsList(@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model) {
		model = addAttributesToModel(model, project);
		return views.project.getDatasourcesList();
	}
	
	@PostMapping(RequestPath.setSource)
	public RedirectView postSourceFile(@RequestParam(MODEL_PostFile) MultipartFile file,
			@SessionAttribute(ProjectController.SESSION_Account) Account account,
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@SessionAttribute(AlgorithmController.SESSION_CurrentAlgorithm) Algorithm algorithm,
			@ModelAttribute(MODEL_HasHeader) HasHeaderCheckbox hasHeader,
			RedirectAttributes ra) {
		
//		DataPreview preview = fileService.store(DataStorageService.relativePath(account, project), file);
		log.debug("HasHeader checkbox state: {}", hasHeader.isHasHeader());
		log.debug("-== Receiving file: {}", file.getName());
		
		MetaData metaData = dataStorageService.saveData(account, project, file, hasHeader.isHasHeader());
		ra.addFlashAttribute(MODEL_MetaData, Optional.of(metaData));
		
//		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
//				.buildAndExpand(project.getName());
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
				.buildAndExpand(project.getName(), algorithm.getName());
		
		log.debug("-== Saving complete");
		return new RedirectView(uriComponents.toUriString());
	}
	
	@PostMapping(RequestPath.setSourceAttributes)
	public RedirectView postSourceAttributes(
			@Valid @ModelAttribute(MODEL_MetaData) MetaData metaData, 
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project) {
		
		log.debug("Saving properties of MetaData: {}", metaData);
		log.debug("ID: {}", metaData.getId());
		log.debug("-== Getting data attributes comlete");
		
//		UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL)
//				.buildAndExpand(projName);
//		UriComponents uriComponents = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
//				.buildAndExpand(project.getName(), algorithm.getName());
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(ProjectController.URL_GetPorject)
				.buildAndExpand(project.getName());
		
		return new RedirectView(uriComponents.toUriString());
	}

}
