package evm.dmc.web.controllers.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.exceptions.FunctionNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.JsonService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.dto.TreeNodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(AlgorithmController.BASE_URL)    // /project/{projectName}/algorithm
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
    private final static String URL_PART_SELECT_FUNCTION = "/selectFunction";
    private final static String URL_PART_GET_FUNCTION_DETAILS = "/getfunction";

    public final static String BASE_URL = ProjectController.URL_GetPorject + URL_PART_ALGORITHM;
    public final static String URL_Add_Algorithm = BASE_URL + URL_PART_ADD_ALGORITHM;
    public final static String URL_Del_Algorithm = BASE_URL + URL_PART_DELETE_ALGORITHM;
    public final static String URL_ModifyAttributes = BASE_URL + URL_PART_MODIFY_ATTRIBUTES;
    public final static String URL_Select_DataSet = BASE_URL + URL_PART_SELECT_DATASET;
    public final static String URL_Select_Function = BASE_URL + URL_PART_SELECT_FUNCTION;
    public final static String URL_GetFunctionDetails = BASE_URL + URL_PART_GET_FUNCTION_DETAILS;

    public static final String PATH_VAR_AlgorithmName = "algName";
    public static final String PATH_VAR_DataName = DatasetController.PATH_VAR_DataName;
    public static final String PATH_VAR_AlgorithmStep = "algstep";

    public static final String PATH_AlgorithmName = "/{" + PATH_VAR_AlgorithmName + "}";
    public static final String PATH_DataName = DatasetController.PATH_DataName;
    public static final String PATH_AlgorithmStep = "/{" + PATH_VAR_AlgorithmStep + "}";

    public final static String SESSION_CurrentAlgorithm = "currentAlgorithm";

    public final static String FLASH_Method = "currentMethod";

    public final static String REQPARAM_Method_Id = "mid";

    public final static String MODEL_PagesMap = "pagesMap";
    public static final String MODEL_AlgBaseURL = "algBaseURL";
    public static final String MODEL_SelDataURL = "selDataURL";
    public static final String MODEL_URL_DelAlgorithm = "urlAlgDelete";
    public static final String MODEL_URL_SelectFunction = "urlSelFunction";
    public static final String MODEL_AlgorithmsList = "algorithmsSet";
    public static final String MODEL_NewAlgorithm = "newAlgorithm";
    public static final String MODEL_FunctionsList = "functionsList";
    public static final String MODEL_MethodDetails = "methodDetails";

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private Views views;

    @Autowired
    private AlgorithmModelAppender modelAppender;

    @Autowired
    private DatasetModelAppender datasetModelAppender;

    @Autowired
    private JsonService jsonService;

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

    @GetMapping(PATH_AlgorithmName)    // /{algName}
    public String getAlgorithm(
            @PathVariable(PATH_VAR_AlgorithmName) String algName,
            @ModelAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
            Model model,
            HttpServletRequest request) throws AlgorithmNotFoundException, JsonProcessingException {

        log.trace("-== Getting algorithm");
        Optional<Algorithm> optAlgorithm = algorithmService.getByProjectAndName(project, algName);
        Algorithm algorithm = optAlgorithm.orElseThrow(() ->
                new AlgorithmNotFoundException("No such algorithm" + algName + " in project " + project.getName()));

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
        if (bindingResult.hasErrors()) {
            log.error("-== Invalid new algorithm's property: {}", bindingResult);

            new RedirectView(BASE_URL);
        }

        log.trace("-== Adding algorithm: {}", algorithm.getName());
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

    @GetMapping(URL_PART_SELECT_DATASET + PATH_DataName)
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


    @GetMapping(URL_PART_GET_FUNCTION_DETAILS)
    public String getFunctionDetails(
            @SessionAttribute(SESSION_CurrentAlgorithm) Algorithm algorithm,
            @ModelAttribute(FLASH_Method) Optional<PatternMethod> optMethod,
            @RequestParam(value = REQPARAM_Method_Id, required = false) Long id,
            Model model
    ) {
        model.addAttribute(MODEL_MethodDetails, optMethod.orElseGet(() -> algorithmService.getMethod(algorithm, id)
                .orElseThrow(() -> new FunctionNotFoundException("There is no method with id = " + id))));

        return views.getProject().algorithm.methodDetails;
    }

    @PostMapping(value = URL_PART_SELECT_FUNCTION + PATH_AlgorithmStep, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RedirectView postSelectedFunction(
            @SessionAttribute(SESSION_CurrentAlgorithm) Algorithm algorithm,
            @SessionAttribute(ProjectController.SESSION_CurrentProject) ProjectModel project,
            @RequestBody TreeNodeDTO function,
            @PathVariable(PATH_VAR_AlgorithmStep) Integer step,
            RedirectAttributes ra,
            Model model
    ) {
        log.debug("RequestParam: {}", step);
        log.debug("Returned DTO: {}", function);
        algorithm = algorithmService.setMethod(algorithm, function, step);
        model.addAttribute(AlgorithmController.SESSION_CurrentAlgorithm, algorithm);
        PatternMethod method = algorithmService.getStep(algorithm, step);

        ra.addFlashAttribute(FLASH_Method, Optional.of(method));

        return new RedirectView(cookURL(URL_GetFunctionDetails, project.getName()));
    }

    @GetMapping("tabs")
    public String getTabs() {
        return "project/algorithm/bootstrap-tabs";
    }

    @GetMapping("alg0/algtest")
    public String getTestAlg() {
        return "project/algorithm/algTest";
    }

    private static String cookURL(String URL, String projectName) {
        return UriComponentsBuilder.fromPath(URL)
                .buildAndExpand(projectName)
                .toUriString();
    }
}
