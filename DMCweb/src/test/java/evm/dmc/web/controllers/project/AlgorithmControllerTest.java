package evm.dmc.web.controllers.project;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;
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
public class AlgorithmControllerTest {
	
	@MockBean
	DataStorageService fileService;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	Views views;

	@Test
	@WithMockUser("devel")
	public final void testGetAlgorithmDefault() throws Exception {
		String projectName = "testProject";
		String algName = "testAlg";
		ProjectModel testProject = new ProjectModel();
		AlgorithmModel testAlg = new AlgorithmModel();
		testAlg.setName(algName);
		
		testProject.setName(projectName);
		testProject.assignAlgorithm(testAlg);
		
		mockMvc.perform(get(RequestPath.project+"/"+projectName + RequestPath.algorithm+"/"+algName)
				.sessionAttr(AlgorithmController.SESSION_CurrProject, testProject))
			.andExpect(status().isOk())
			.andExpect(view().name(views.project.wizard.datasource))
			.andExpect(model().attributeExists(
					AlgorithmController.MODEL_SrcUploadURI,
					AlgorithmController.MODEL_SrcAttrURI,
					AlgorithmController.MODEL_HasHeader))
			;
	}

	@Test
	@WithMockUser("Alex")
	public final void testPostSourceFile() throws Exception {
		ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());

		MockMultipartFile file = new MockMultipartFile("file", resource.getInputStream());
		
		ArgumentCaptor<Account> accCaptor = ArgumentCaptor.forClass(Account.class);
		ArgumentCaptor<ProjectModel> projCaptor = ArgumentCaptor.forClass(ProjectModel.class);
		ArgumentCaptor<MultipartFile> fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
		Mockito
			.when(fileService.saveData(accCaptor.capture(), projCaptor.capture(), 
					fileCaptor.capture(), any(boolean.class)))
			.thenReturn(new MetaData());
		
		UriComponents uriComponents = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
				.path(RequestPath.setSource)
				.buildAndExpand("tesrpr", "testalg");
		
		log.debug("Request to: {}", uriComponents.toUriString());
		
		ProjectModel testProject = new ProjectModel();
		testProject.setName("tesrpr");
		
		
		this.mockMvc
			.perform(
					MockMvcRequestBuilders
						.fileUpload(uriComponents.toUriString())
						.file(file)
						.sessionAttr("account", new Account("Alex"))
						.sessionAttr("currentProject", testProject))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
						.buildAndExpand("tesrpr", "testalg")
						.toUriString()))
			.andExpect(flash().attributeExists(
					AlgorithmController.MODEL_MetaData))
			;
			
		log.debug("File Path: {}", accCaptor.getValue());
		log.debug("File name {}", fileCaptor.getValue().getOriginalFilename());
		assertThat(accCaptor.getValue().getUserName(), equalTo("Alex"));
		assertThat(projCaptor.getValue().getName(), equalTo("tesrpr"));
	}

}
