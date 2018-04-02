package evm.dmc.web.controllers.project;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Views.class)
@Slf4j
public class DatasetControllerTest {
	
	@MockBean
	private DataStorageService dataStorageService;
	
	@Autowired
	private MockMvc mockMvc;
	

	@Test
	@WithMockUser("Alex")
	public final void testPostSourceFile() throws Exception {
		final String TEST_PROJ_NAME = "testProj";
		final String TEST_USER_NAME = "Alex";
		final String TEST_ALG_NAME = "testAlg";
		
		ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());

		MockMultipartFile file = new MockMultipartFile("file", resource.getInputStream());
		
		ArgumentCaptor<Account> accCaptor = ArgumentCaptor.forClass(Account.class);
		ArgumentCaptor<ProjectModel> projCaptor = ArgumentCaptor.forClass(ProjectModel.class);
		ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
		Mockito
			.when(dataStorageService.saveData(accCaptor.capture(), projCaptor.capture(), 
					fileCaptor.capture(), null))
			.thenReturn(new MetaData());
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(DatasetController.BASE_URL)
				.path(RequestPath.setSource)
				.buildAndExpand("tesrpr");
		
		log.debug("Request to: {}", uriComponents.toUriString());
		
		ProjectModel testProject = new ProjectModel();
		testProject.setName(TEST_PROJ_NAME);
		
		Algorithm testAlg = AlgorithmService.getNewAlgorithm();
		testAlg.setName(TEST_ALG_NAME);
		
		this.mockMvc
			.perform(
					MockMvcRequestBuilders
						.fileUpload(uriComponents.toUriString())
						.file(file)
						.sessionAttr(ProjectController.SESSION_Account, new Account(TEST_USER_NAME))
						.sessionAttr(ProjectController.SESSION_CurrentProject, testProject)
						.sessionAttr(AlgorithmController.SESSION_CurrentAlgorithm, testAlg))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
						.buildAndExpand(TEST_PROJ_NAME, TEST_ALG_NAME)
						.toUriString()))
			.andExpect(flash().attributeExists(
					DatasetController.MODEL_MetaData))
			;
			
		log.debug("File Path: {}", accCaptor.getValue());
		log.debug("File name {}", fileCaptor.getValue().getOriginalFilename());
		assertThat(accCaptor.getValue().getUserName(), equalTo(TEST_USER_NAME));
		assertThat(projCaptor.getValue().getName(), equalTo(TEST_PROJ_NAME));
	}

}
