package evm.dmc.web.controllers.project;

import static evm.dmc.web.service.AlgorithmService.getNewAlgorithm;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.config.SecurityConfig;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Views.class)
@Slf4j
public class AlgorithmControllerTest {
	
	@MockBean
	private AlgorithmService algorithmService;
	
	@MockBean
	private MetaData metaData;
	
	@MockBean
	private MetaDataService metaDataService;
	
//	@MockBean
//	private DatasetController datasetController;
	
//	@MockBean
//	private Model model;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private Views views;

	@Test
	@WithMockUser("Alex")
	public final void testGetAlgorithmDefault() throws Exception {
		final String TEST_PROJECT_NAME = "testProject";
		final String TEST_ALG_NAME = "testAlg";
		final String TEST_USER_NAME = "Alex";
		
		ProjectModel testProject = new ProjectModel();
		Algorithm testAlg = getNewAlgorithm();
		
		testAlg.setName(TEST_ALG_NAME);
		
		testProject.setName(TEST_PROJECT_NAME);
		testProject.getAlgorithms().add(testAlg);
		testAlg.setParentProject(testProject);
		String URL = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
				.buildAndExpand(TEST_PROJECT_NAME,TEST_ALG_NAME).toString();
		
		Mockito
			.when(algorithmService.getByProjectAndName(testProject, TEST_ALG_NAME))
			.thenReturn(Optional.of(testAlg));
		
		Set<MetaData> dataSet = new HashSet<>();
		dataSet.add(new MetaData());
		Mockito
			.when(metaDataService.getForProject(testProject))
			.thenReturn(dataSet);
		
		
//		ArgumentCaptor<Model> modelCaptor = ArgumentCaptor.forClass(Model.class);
//		Mockito
//			.when(datasetController.addAttributesToModel(modelCaptor.capture(), testProject, Optional.of(metaData)))
//			.thenReturn(addArguments(modelCaptor.getValue()));
		
		mockMvc.perform(get(URL)
				.sessionAttr(ProjectController.SESSION_CurrentProject, testProject))
			.andExpect(status().isOk())
			.andExpect(view().name(views.project.wizard.datasource))
			.andExpect(model().attributeExists(
					DatasetController.MODEL_DataUploadURL,
					DatasetController.MODEL_DataAttributesURL,
					DatasetController.MODEL_HasHeader))
			;
	}
	
		
//	@Test
//	@WithMockUser("Alex")
//	public final void testGetAlgorithmWithMetaData() throws Exception {
//		String projectName = "testProject";
//		String algName = "testAlg";
//		ProjectModel testProject = new ProjectModel();
//		AlgorithmModel testAlg = new AlgorithmModel();
//		
//		testAlg.setName(algName);
//		
//		testProject.setName(projectName);
//		testProject.assignAlgorithm(testAlg);
//		
//		Mockito.when(dataStorage.isHasHeader()).thenReturn(true);
//		Mockito.when(metaData.getStorage()).thenReturn(dataStorage);
//		Optional<MetaData> optMetaData = Optional.of(metaData);
//		
//		Mockito
//		.when(metaDataService.getPreview(any(MetaData.class)))
//		.thenReturn(new DataPreview());
//		
//		mockMvc.perform(get(RequestPath.project+"/"+projectName + RequestPath.algorithm+"/"+algName)
//				.sessionAttr(AlgorithmController.SESSION_CurrProject, testProject)
//				.sessionAttr(AlgorithmController.SESSION_Account, new Account("Alex"))
//				.sessionAttr(AlgorithmController.MODEL_MetaData, optMetaData))
//			.andExpect(status().isOk())
//			.andExpect(view().name(views.project.wizard.datasource))
////			.andExpect(model().attribute(AlgorithmController.MODEL_MetaData, metaData))
//			.andExpect(model().attributeExists(
//					AlgorithmController.MODEL_SrcUploadURI,
//					AlgorithmController.MODEL_SrcAttrURI,
//					AlgorithmController.MODEL_HasHeader,
//					AlgorithmController.MODEL_Preview))
//			;
//	}

	

}
