package evm.dmc.web.controllers.project;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.MetaDataNotFoundException;
import evm.dmc.web.service.DataSetProperties;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(DatasetController.BASE_URL)
@SessionAttributes({ProjectController.SESSION_Account, 
	ProjectController.SESSION_CurrentProject,
	AlgorithmController.SESSION_CurrentAlgorithm})
@Slf4j
public class DatasetController {
	public static final String URL_PART_DATASET = "/dataset";
	public static final String URL_PART_SETSRC = "/setsrc";
//	public static final String URL_PART_GETSRC = "/getsrc";
	public static final String URL_PART_DELSRC = "/delsrc";
	public static final String URL_PART_SETATTRIBUTES = "/setsrcattr";
//	public static final String URL_PART_GETPREVIEW = "/getpreview";
	
	public static final String PATH_VAR_DataName = "dataName";
	public static final String PATH_DataName = "/{" + PATH_VAR_DataName + ":.+}";
	
	public static final String BASE_URL=ProjectController.URL_GetPorject + URL_PART_DATASET;
//	public static final String URL_GetSource = BASE_URL + URL_PART_GETSRC;
	public static final String URL_SetSource = BASE_URL + URL_PART_SETSRC;
	public static final String URL_DeleteSource = BASE_URL + URL_PART_DELSRC;
	public static final String URL_SetAttributes = BASE_URL + URL_PART_SETATTRIBUTES;
//	public static final String URL_GetPreview = BASE_URL + URL_PART_GETPREVIEW;
	
	public static final String MODEL_AllowSelectFeatures = "AllowSelectFeatures";
	public static final String MODEL_SelectedNamesBean = "selectedNames";
	public static final String MODEL_DataSets = "dataSets";
	public final static String MODEL_PostFile = "file";
	public final static String MODEL_MetaData = "metaData";
	public static final String MODEL_DataBaseURL = "dataBaseURL";
	public final static String MODEL_DataUploadURL = "dataUploadURL";
	public final static String MODEL_DataAttributesURL = "dataAttributesURL";
	public final static String MODEL_DataSetProps = "dataSetProps";
	public final static String MODEL_Preview = "preview";
	public final static String MODEL_ShowChekboxes = "showCheckboxes";
	public final static String MODEL_ActionURL = "actionURL";

	public final static String REQPARAM_ShowCheckboxes = MODEL_ShowChekboxes;
	public final static String REQPARAM_ActionURL = MODEL_ActionURL;
	
	public final static String FLASH_MetaData = "metaData";
	

//	@Autowired
	private DataStorageService dataStorageService;
	
//	@Autowired
	private MetaDataService metaDataService;
	
	@Autowired DatasetModelAppender modelAppender;
	
//	@Autowired
	private Views views;
	
	@ModelAttribute(ProjectController.MODEL_BackBean)
	public CheckboxNamesBean backingBeanForCheckboxes() {
		return new CheckboxNamesBean();
	}
	
	public DatasetController(@Autowired DataStorageService dataStorageService,
			@Autowired MetaDataService metaDataService, 
			@Autowired Views views) {
		super();
		this.dataStorageService = dataStorageService;
		this.metaDataService = metaDataService;
		this.views = views;
	}

	
	@GetMapping
	public String getDataSetsList(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model) {
		model = modelAppender.addAttributesToModel(model, project);
		return views.project.getDatasourcesList();
	}
	
	@GetMapping(PATH_DataName)
	public String GetDataSet(@PathVariable(PATH_VAR_DataName) String dataName,
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@RequestParam(value = REQPARAM_ShowCheckboxes, defaultValue = "false") Boolean showCheckboxes,
			@RequestParam(value = REQPARAM_ActionURL, defaultValue = "") String actionURL,
			@ModelAttribute(FLASH_MetaData) Optional<MetaData> optMetaData,
			Model model
			) {
		log.debug("-== Looking for {}", dataName);
		Optional<MetaData> optMeta = Optional.ofNullable(
				optMetaData.orElseGet( ()->
						metaDataService.getByProjectAndName(project, dataName)
							.orElseThrow(() -> 
								new MetaDataNotFoundException("MetaData " + dataName+ " not found"))));
		
		log.debug("-== Opt MetaData: {}", optMeta);
	
		model = modelAppender.addAttributesToModel(model, project, optMeta);
		model.addAttribute(MODEL_ShowChekboxes, showCheckboxes);
		if(actionURL.isEmpty())
			model.addAttribute(MODEL_ActionURL, URL_SetAttributes);
		else
			model.addAttribute(MODEL_ActionURL, actionURL);
		return views.project.data.dataSource;
	}
	
	@PostMapping(URL_PART_SETSRC)
	public RedirectView postSourceFile(@RequestParam(MODEL_PostFile) MultipartFile file,
			@SessionAttribute(ProjectController.SESSION_Account) Account account,
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@ModelAttribute(MODEL_DataSetProps) DataSetProperties datasetProps,
			RedirectAttributes ra,
			HttpServletRequest request) {
		
		log.trace("-== HasHeader checkbox state: {}", datasetProps.isHasHeader());
		log.trace("-== Etered name: {}", datasetProps.getName());
		log.trace("-== Receiving file: {}", file.getName());
		
		MetaData metaData = dataStorageService.saveData(account, project, file, datasetProps);
		ra.addFlashAttribute(MODEL_MetaData, Optional.of(metaData));
		
		log.debug("-== Saving complete");
		return new RedirectView(request.getHeader("Referer"));
	}
	
	@PostMapping(URL_PART_SETATTRIBUTES)
	public RedirectView postModifyAttributes(
			@Valid @ModelAttribute(MODEL_MetaData) MetaData metaData, 
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project) {
		
		log.debug("Saving properties of MetaData: {}", metaData);
		log.debug("ID: {}", metaData.getId());
		log.debug("-== Getting data attributes comlete");
		
		metaDataService.updateAttributes(project, metaData);
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(ProjectController.URL_GetPorject)
				.buildAndExpand(project.getName());
		
		return new RedirectView(uriComponents.toUriString());
	}
	
	@PostMapping(URL_PART_DELSRC)
	public RedirectView postDeleteData(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@ModelAttribute(MODEL_SelectedNamesBean) CheckboxNamesBean checkboxNames,
			HttpServletRequest request) {
		log.debug("==Deleteion datasets with names: {}",Arrays.asList(checkboxNames.getNames()));
		
		dataStorageService.delete(project, checkboxNames.getNamesSet());
		
		return new RedirectView(request.getHeader("Referer"));
	}
	
//	@PostMapping(URL_PART_GETPREVIEW)
//	public RedirectView postGetPreview(
//			HttpServletRequest request) {
//		return new RedirectView(request.getHeader("Referer"));
//	}

}
