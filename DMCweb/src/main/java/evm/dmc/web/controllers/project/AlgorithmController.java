package evm.dmc.web.controllers.project;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(AlgorithmController.BASE_URL)	// /project/{projectName}/algorithm
@SessionAttributes({ProjectController.SESSION_Account, 
	ProjectController.SESSION_CurrentProject,
	AlgorithmController.SESSION_CurrentAlgorithm})
@Slf4j
public class AlgorithmController {
	private final static String URL_PART_ALGORITHM = "/algorithm";
	private final static String URL_PART_MODIFY_ATTRIBUTES = "/modifyAttrs";
	private final static String URL_PART_SELECT_DATASET = "/selectDataset";
	private final static String URL_PART_DELETE_ALGORITHM = "/delete";
	private final static String URL_PART_ADD_ALGORITHM = "/add";
//	private final static String URL_PART_GET_FUNCTIONS_LIST = "/getfunlist";
	
	public final static String BASE_URL = ProjectController.URL_GetPorject + URL_PART_ALGORITHM;
	public final static String URL_Add_Algorithm = BASE_URL + URL_PART_ADD_ALGORITHM;
	public final static String URL_Del_Algorithm = BASE_URL + URL_PART_DELETE_ALGORITHM;
	public final static String URL_ModifyAttributes = BASE_URL + URL_PART_MODIFY_ATTRIBUTES;
	public final static String URL_Select_DataSet = BASE_URL + URL_PART_SELECT_DATASET;
//	public final static String URL_Get_Functions_List = BASE_URL + URL_PART_GET_FUNCTIONS_LIST;
	
	public static final String PATH_VAR_AlgorithmName = "algName";
	public static final String PATH_VAR_DataName = DatasetController.PATH_VAR_DataName;
	public static final String PATH_AlgorithmName = "/{" + PATH_VAR_AlgorithmName + "}";
	public static final String PATH_DataName = DatasetController.PATH_DataName;
	
	public final static String SESSION_CurrentAlgorithm = "currentAlgorithm";
	
//	public final static String MODEL_Algorithm = "algorithm";
	public final static String MODEL_PagesMap = "pagesMap";
	public static final String MODEL_AlgBaseURL = "algBaseURL";
	public static final String MODEL_SelDataURL = "selDataURL";
	public static final String MODEL_URL_DelAlgorithm = "algDelete";
	public static final String MODEL_AlgorithmsList = "algorithmsSet";
	public static final String MODEL_NewAlgorithm = "newAlgorithm";
	public static final String MODEL_FunctionsList = "functionsList";
//	public static final String MODEL_CurrentAlgorithm = "currentAlgorithm";
	
	@Autowired
	private AlgorithmService algorithmService;
	
//	@Autowired
//	private DatasetController datasetController;
	
	@Autowired
	private Views views;
	
	@Autowired
	private AlgorithmModelAppender modelAppender;
	
	@Autowired
	private DatasetModelAppender datasetModelAppender;
	
	@ModelAttribute(ProjectController.MODEL_BackBean)
	public CheckboxNamesBean backingBeanForCheckboxes() {
		return new CheckboxNamesBean();
	}
	
	@GetMapping
	public String getAlgorithmsList(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model) {
		model = modelAppender.addAttributesToModel(model, project);
		return views.project.getAlgorithmsList();
	}
	
	@GetMapping(PATH_AlgorithmName)	// /{algName}
	public String getAlgorithm(
			@PathVariable(PATH_VAR_AlgorithmName) String algName,
			@ModelAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			Model model,
			HttpServletRequest request) throws AlgorithmNotFoundException {
		
		log.trace("-== Getting algorithm");
		Optional<Algorithm> optAlgorithm = algorithmService.getByProjectAndName(project, algName);
		Algorithm algorithm = optAlgorithm.orElseThrow(()->
		new AlgorithmNotFoundException("No such algorithm" + algName + " in project "+ project.getName()));
		
		model.addAttribute(AlgorithmController.SESSION_CurrentAlgorithm, algorithm);
		
		/* IMPORTANT: sequence of subsequent calls is significant:
		 * AlgorithmModelAppender overrides some points on model, 
		 * that was set in DatasetModelAppender
		 */
		datasetModelAppender.addAttributesToModel(model, project);
		modelAppender.addAttributesToModel(model, project, optAlgorithm);
		
		return views.project.algorithm.algorithm;
	}
	
	/**
	 * Handles request to /project/algorithm/add
	 * 
	 * @param project
	 * @param algorithm
	 * @return
	 */
	@PostMapping(URL_PART_ADD_ALGORITHM)
	public RedirectView postAddAlgorithm(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@Valid @ModelAttribute(MODEL_NewAlgorithm) Algorithm algorithm, 
			BindingResult bindingResult
			) {
		if(bindingResult.hasErrors()) {
			log.error("-== Invalid new algorithm's property: {}", bindingResult);
			
			new RedirectView(BASE_URL);
		}
		
		log.trace("-== Adding algorithm: {}" ,algorithm.getName());
		log.trace("-== to project: {}", project.getId() + project.getName());
		
		algorithm = algorithmService.addNew(project, algorithm);

		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
	@PostMapping(URL_PART_DELETE_ALGORITHM)
	public RedirectView postDelAlgorithm(
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@ModelAttribute(ProjectController.MODEL_BackBean) CheckboxNamesBean bean
			) {
//		log.debug("Selected algorithms for deleteion:{}", StringUtils.arrayToCommaDelimitedString(bean.getNames()));
		log.debug("-== Selected algorithms for deleteion:{}", bean.getNamesSet());
		log.debug("-==Project for deletion in: {}", project);
		
//		projectService.deleteAlgorithms(project, new HashSet<String>(Arrays.asList(bean.getNames())));
		algorithmService.delete(project, bean.getNamesSet());
		
		return new RedirectView(String.format("%s/%s", RequestPath.project, project.getName()));
	}
	
	@GetMapping(URL_PART_SELECT_DATASET+PATH_DataName)
	public RedirectView getSelectDataset(
			@PathVariable(PATH_VAR_DataName) String dataName,
			@SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
			@SessionAttribute(SESSION_CurrentAlgorithm) Algorithm algorithm,
			@RequestParam(value = DatasetController.REQPARAM_ShowCheckboxes, defaultValue = "true") Boolean showCheckboxes,
			RedirectAttributes ra
			) {
		log.debug("-== Selecting dataset: {}", dataName);
		
		algorithm = algorithmService.setDataSource(algorithm, dataName);
		
		// override default attributes map to use it on preview page 
		algorithm.getDataSource().setAttributes(algorithm.getSrcAttributes());
		
		// adding optional to flash attributes, it will be extracted in DatasetController when add preview
		ra.addFlashAttribute(DatasetController.FLASH_MetaData, Optional.of(algorithm.getDataSource()));
		
		// redirect showCheckboxes flag to DatasetController
		ra.addAttribute(DatasetController.REQPARAM_ShowCheckboxes, showCheckboxes);
		
		ra.addAttribute(DatasetController.REQPARAM_ActionURL, URL_ModifyAttributes);
		
		UriComponents uri = UriComponentsBuilder
				.fromPath(DatasetController.BASE_URL + "/" + dataName)
				.buildAndExpand(project.getName());
		
		/* redirect to DatasetController.getDataSet: /project/{projectName}/dataset/{dataName} */
		return new RedirectView(uri.toString());
	}
	
	@PostMapping(URL_PART_MODIFY_ATTRIBUTES)
	public RedirectView postSaveDataAtributes(
			@SessionAttribute(SESSION_CurrentAlgorithm) Algorithm algorithm,
			@Valid @ModelAttribute(DatasetController.MODEL_MetaData) MetaData metaData,
			HttpServletRequest request
			) {
		log.trace("-== Saving properties of MetaData: {}", metaData);
		
		algorithmService.setAttributes(algorithm, metaData);
		
		return new RedirectView(request.getHeader("Referer"));
	}
	
	@GetMapping("/gettree") 
	public String getTestTreePage() {
		
		return "project/algorithm/algTest";
	}
	
}
