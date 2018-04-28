package evm.dmc.web.controllers.project;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.web.service.DataSetProperties;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@WithMockUser("devel")
@EnableConfigurationProperties(Views.class)
public class DatasetControllerTest {
	final private String TEST_ALG_NAME = "alg0";
	
	@MockBean
	private DataStorageService dataStorageService;
	
	@MockBean
	private MetaDataService metaDataService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private Views views;
	
	private ProjectModel testSessionProject = DatasetTestUtils.getProjectModel(DatasetTestUtils.getAccount());
//	private Algorithm testSessionAlgorithm = AlgorithmService.getNewAlgorithm();
	
	private ArgumentCaptor<Account> accCaptor = ArgumentCaptor.forClass(Account.class);
	private ArgumentCaptor<ProjectModel> projCaptor = ArgumentCaptor.forClass(ProjectModel.class);
	
	
	@Before
	public final void init() {
	}
	
	@Test
	public final void testGetDataSetsList() throws Exception {
		URI uri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL)
				.buildAndExpand(DatasetTestUtils.TEST_PROJ_NAME).toUri();
		
		MetaData  meta = DatasetTestUtils.getMetaData(testSessionProject);
		
		List<MetaData> metaDataList = new LinkedList<>();
		metaDataList.add(meta);
		Mockito
			.when(metaDataService.getForProjectSortedBy(projCaptor.capture(), eq("name")))
			.thenReturn(metaDataList);
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get(uri.toString())
					.sessionAttr(ProjectController.SESSION_CurrentProject, testSessionProject))
			.andExpect(status().isOk())
			.andExpect(view().name(views.project.getDatasourcesList()))
			// model from controller
			.andExpect(model().attributeExists(ProjectController.MODEL_BackBean))
			// models from DatasetModelAppender.addAttributesToModel()
			.andExpect(model().attribute(DatasetController.MODEL_DataSets, metaDataList))
			.andExpect(model().attributeExists(DatasetController.MODEL_DataSetProps))
			.andExpect(model().attributeExists(DatasetController.MODEL_SelectedNamesBean))
			// URLs from DatasetModelAppender.setURLs()
			.andExpect(model().attribute(DatasetController.MODEL_DataBaseURL, uri.toString()))
			.andExpect(model().attribute(DatasetController.MODEL_DataAttributesURL, 
					UriComponentsBuilder.fromPath(DatasetController.URL_SetAttributes)
						.buildAndExpand(testSessionProject.getName())
						.toString()))
			;
		assertThat(projCaptor.getValue().getName(), equalTo(DatasetTestUtils.TEST_PROJ_NAME));
	}
	
	@Test
	public final void testGetDataSet_Without_FLASH_MetaData() throws Exception {
		URI uri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL + DatasetController.PATH_DataName)
				.buildAndExpand(DatasetTestUtils.TEST_PROJ_NAME, DatasetTestUtils.TEST_DATA_NAME).toUri();
		
		MetaData  meta = DatasetTestUtils.getMetaData(testSessionProject);
		Mockito
			.when(metaDataService.getByProjectAndName(projCaptor.capture(), eq(DatasetTestUtils.TEST_DATA_NAME)))
			.thenReturn(Optional.of(meta));
		
		DataPreview preview = DatasetTestUtils.getDataPreview();
		Mockito
			.when(dataStorageService.getPreview(eq(meta)))
			.thenReturn(preview);
		
		DataStorageModel dataStorage = DatasetTestUtils.getDataStorageModel();
		Mockito
			.when(dataStorageService.getDataStorage(eq(meta)))
			.thenReturn(dataStorage);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(uri.toString())
				.sessionAttr(ProjectController.SESSION_CurrentProject, testSessionProject))
		.andExpect(status().isOk())
		.andExpect(view().name(views.project.data.dataSource))
		.andExpect(model().attribute(DatasetController.MODEL_ShowChekboxes, false))
		.andExpect(model().attribute(DatasetController.MODEL_ActionURL, DatasetController.URL_SetAttributes))
		.andExpect(model().attribute(DatasetController.MODEL_MetaData, meta))
		.andExpect(model().attribute(DatasetController.MODEL_Preview, preview))
		.andExpect(model().attributeExists(DatasetController.MODEL_DataSetProps))
		;
		
	}
	
	@Test
	public final void testGetDataSet_With_FLASH_MetaData() throws Exception {
		URI uri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL + DatasetController.PATH_DataName)
				.buildAndExpand(DatasetTestUtils.TEST_PROJ_NAME, DatasetTestUtils.TEST_DATA_NAME).toUri();
		MetaData  meta = DatasetTestUtils.getMetaData(testSessionProject);
		
		DataPreview preview = DatasetTestUtils.getDataPreview();
		Mockito
			.when(dataStorageService.getPreview(eq(meta)))
			.thenReturn(preview);
		
		DataStorageModel dataStorage = DatasetTestUtils.getDataStorageModel();
		Mockito
			.when(dataStorageService.getDataStorage(eq(meta)))
			.thenReturn(dataStorage);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(uri.toString())
				.sessionAttr(ProjectController.SESSION_CurrentProject, testSessionProject)
				.flashAttr(DatasetController.FLASH_MetaData, Optional.of(meta)))
		.andExpect(status().isOk())
		.andExpect(view().name(views.project.data.dataSource))
		.andExpect(model().attribute(DatasetController.MODEL_ShowChekboxes, false))
		.andExpect(model().attribute(DatasetController.MODEL_ActionURL, DatasetController.URL_SetAttributes))
		.andExpect(model().attribute(DatasetController.MODEL_MetaData, meta))
		.andExpect(model().attribute(DatasetController.MODEL_Preview, preview))
		.andExpect(model().attributeExists(DatasetController.MODEL_DataSetProps))
		;
		
	}
	
	
	@Test
	public final void testPostSourceFile() throws Exception {
		UriComponents uri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL)
				.path(RequestPath.setSource)
				.buildAndExpand(DatasetTestUtils.TEST_PROJ_NAME);
		
		final String filename = "iris.csv";
		ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile(DatasetController.MODEL_PostFile, 
				resource.getInputStream());
		
		ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
		
		Mockito
			.when(dataStorageService.saveData(accCaptor.capture(), projCaptor.capture(), 
					fileCaptor.capture(), any(DataSetProperties.class)))
			.thenReturn(MetaData.builder().build());
		
		String expectedUrl = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
				.buildAndExpand(DatasetTestUtils.TEST_PROJ_NAME, TEST_ALG_NAME)
				.toUriString();
		
		this.mockMvc
			.perform(
					MockMvcRequestBuilders
						.fileUpload(uri.toUriString())
						.file(file)
						.sessionAttr(ProjectController.SESSION_Account, DatasetTestUtils.getAccount())
						.sessionAttr(ProjectController.SESSION_CurrentProject, testSessionProject)
						.header("Referer", expectedUrl))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(expectedUrl))
			.andExpect(flash().attributeExists(
					DatasetController.MODEL_MetaData))
			;
			
		assertThat(accCaptor.getValue().getUserName(), equalTo(DatasetTestUtils.TEST_USER_NAME));
		assertThat(projCaptor.getValue().getName(), equalTo(DatasetTestUtils.TEST_PROJ_NAME));
	}

	@Test
	public final void testPostModifyAttributes() throws Exception {
		
	}
	
	@Test
	public final void testPostDeleteData() throws Exception {
		
	}
}
