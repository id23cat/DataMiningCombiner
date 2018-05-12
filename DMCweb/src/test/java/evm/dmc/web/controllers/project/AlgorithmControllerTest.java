package evm.dmc.web.controllers.project;

import static evm.dmc.web.controllers.project.utils.AlgorithmTestUtils.*;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.Decoder.Text;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.JsonService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.dto.TreeNodeDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Views.class)
@WithMockUser("devel")
public class AlgorithmControllerTest {
	
	@MockBean
	private AlgorithmService algorithmService;
	
	@MockBean
	private MetaData metaData;
	
	@MockBean
	private MetaDataService metaDataService;
	
	@Autowired
	private JsonService jsonService;
	
	@MockBean
	private DataStorageService dataStorageService;
	
//	@MockBean
//	private DatasetController datasetController;
	
//	@MockBean
//	private Model model;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private Views views;
	
	@Test
	public final void testGetAlgorithmsList() throws Exception {
		String url = cookURL(AlgorithmController.BASE_URL, TEST_PROJ_NAME);
		
		Mockito
			.when(algorithmService.getForProjectSortedBy(eq(TEST_session_project), eq("name")))
			.thenReturn(TEST_algList);
		
		ResultActions checkResult = this.mockMvc.perform(get(url)
				.sessionAttr(ProjectController.SESSION_CurrentProject, TEST_session_project))
		.andExpect(status().isOk())
		.andExpect(view().name(views.project.getAlgorithmsList()))
		.andExpect(model().attribute(AlgorithmController.MODEL_AlgorithmsList, TEST_algList))
		.andExpect(model().attributeExists(AlgorithmController.MODEL_NewAlgorithm))
		;
		
		// AlgorithmController URLs:
		assertAlgorithmURLs(checkResult, TEST_PROJ_NAME);
	}

	@Test
	public final void testGetAlgorithm_withoutDataSource() throws Exception {
		String url = cookURL(AlgorithmController.BASE_URL+AlgorithmController.PATH_AlgorithmName, 
				TEST_PROJ_NAME, TEST_ALG0_NAME);
		
		Mockito
			.when(algorithmService.getByProjectAndName(eq(TEST_session_project), eq(TEST_ALG0_NAME)))
			.thenReturn(Optional.of(TEST_alg0));
		List<TreeNodeDTO> funcList = getFunctionsListDTO();
		Mockito
			.when(algorithmService.getFrameworksAsTreeNodes())
			.thenReturn(funcList);
		Mockito
			.when(algorithmService.getDataSource(Optional.of(TEST_alg0)))
			.thenReturn(Optional.ofNullable(TEST_alg0.getDataSource()));
		
		ResultActions checkResult = this.mockMvc.perform(get(url)
				.sessionAttr(ProjectController.SESSION_CurrentProject, TEST_session_project))
		.andExpect(status().isOk())
		.andExpect(view().name(views.project.algorithm.algorithm))
		.andExpect(model().attribute(AlgorithmController.SESSION_CurrentAlgorithm, TEST_alg0))
		.andExpect(model().attribute(AlgorithmController.MODEL_FunctionsList, 
				jsonService.frameworksListToTreeView(funcList)))
		;
		
		// model attributes set in addAttributesToModel
		assertAlgorithmAttributesToModel(checkResult, Optional.of(TEST_alg0), null);
		
		// AlgorithmController URLs:
		assertAlgorithmURLs(checkResult, TEST_PROJ_NAME);
	}
	
	@Test
	public final void testGetAlgorithm_withDataSource() throws Exception {
		String url = cookURL(AlgorithmController.BASE_URL+AlgorithmController.PATH_AlgorithmName, 
				TEST_PROJ_NAME, TEST_ALG0_NAME);
		
		MetaData meta = getMetaData(TEST_session_project);
		TEST_alg0.setDataSource(meta);
		Mockito
			.when(algorithmService.getByProjectAndName(eq(TEST_session_project), eq(TEST_ALG0_NAME)))
			.thenReturn(Optional.of(TEST_alg0));
		List<TreeNodeDTO> funcList = getFunctionsListDTO();
		Mockito
			.when(algorithmService.getFrameworksAsTreeNodes())
			.thenReturn(funcList);
		
		Mockito
			.when(algorithmService.getDataSource(Optional.of(TEST_alg0)))
			.thenReturn(Optional.ofNullable(TEST_alg0.getDataSource()));
		
		DataPreview preview = getDataPreview();
		Mockito
			.when(dataStorageService.getPreview(eq(meta)))
			.thenReturn(preview);
		
		ResultActions checkResult = this.mockMvc.perform(get(url)
				.sessionAttr(ProjectController.SESSION_CurrentProject, TEST_session_project))
			.andExpect(status().isOk())
			.andExpect(view().name(views.project.algorithm.algorithm))
			.andExpect(model().attribute(AlgorithmController.SESSION_CurrentAlgorithm, TEST_alg0))
			.andExpect(model().attribute(AlgorithmController.MODEL_FunctionsList, 
					jsonService.frameworksListToTreeView(funcList)))
			;
		
		// model attributes set in addAttributesToModel
		assertAlgorithmAttributesToModel(checkResult, Optional.of(TEST_alg0), preview);
				
		// AlgorithmController URLs:
		assertAlgorithmURLs(checkResult, TEST_PROJ_NAME);
	}
	
	@Test(expected=AlgorithmNotFoundException.class)
	public final void testGetAlgorithm_NotExists() throws Throwable {
		String notExists = "none";
		String url = cookURL(AlgorithmController.BASE_URL+AlgorithmController.PATH_AlgorithmName, 
				TEST_PROJ_NAME, notExists);
		
		Mockito
			.when(algorithmService.getByProjectAndName(eq(TEST_session_project), eq(notExists)))
			.thenReturn(Optional.ofNullable(null));
		try {
		this.mockMvc.perform(get(url)
				.sessionAttr(ProjectController.SESSION_CurrentProject, TEST_session_project))
		;
		} catch (NestedServletException ex) {
			throw ex.getCause();
		}
	}
	
	@Test
	public final void testGetSelectedDataset() throws Exception {
		String url = cookURL(AlgorithmController.URL_Select_DataSet+AlgorithmController.PATH_AlgorithmName, 
				TEST_PROJ_NAME, TEST_DATA_NAME);
		
		Algorithm algorithm = TEST_alg0;
		algorithm.setDataSource(getMetaData(TEST_session_project));
		
		Mockito
			.when(algorithmService.setDataSource(eq(TEST_alg0), eq(TEST_DATA_NAME)))
			.thenReturn(algorithm);
		
		String dataName= "dataname"; 
		
		Map<String, String> urlMap = new HashMap<>();
		urlMap.put(ProjectController.PATH_VAR_ProjectName, TEST_PROJ_NAME);
		urlMap.put(dataName, TEST_DATA_NAME);
		
		String expectedUrl = UriComponentsBuilder
			.fromPath(DatasetController.BASE_URL + "/{" + dataName +"}")
			.queryParam(DatasetController.REQPARAM_ShowCheckboxes, "true")
			.queryParam(DatasetController.REQPARAM_ActionURL, 
					URLEncoder.encode(AlgorithmController.URL_ModifyAttributes, "UTF-8"))
			.buildAndExpand(urlMap)
			.toUriString()
		;
		
		this.mockMvc.perform(get(url)
				.sessionAttr(ProjectController.SESSION_CurrentProject, TEST_session_project)
				.sessionAttr(AlgorithmController.SESSION_CurrentAlgorithm, TEST_alg0)
				.param(DatasetController.REQPARAM_ShowCheckboxes, "true"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(expectedUrl))
			.andExpect(flash().attribute(DatasetController.FLASH_MetaData, Optional.of(algorithm.getDataSource())))
		;
	}
	
	
//	@Test
//	public final void testPostSaveDataAtributes() throws Exception {
//		String url = DatasetTestUtils.cookURL(AlgorithmController.URL_ModifyAttributes, 
//				DatasetTestUtils.TEST_PROJ_NAME);
//		
//		String expectedUrl = cookURL(AlgorithmController.BASE_URL,
//				DatasetTestUtils.TEST_PROJ_NAME, TEST_ALG0_NAME);
//		
//		MetaData meta = getMetaData(TEST_session_project);
//		
//		this.mockMvc
//			.perform(post(url)
//					.with(csrf())
//					.content(meta)
//					.sessionAttr(AlgorithmController.SESSION_CurrentAlgorithm, TEST_alg0)
//					
//					
//	}
	
	

}
