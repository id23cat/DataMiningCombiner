package evm.dmc.web.controllers.project;

import static evm.dmc.web.service.AlgorithmService.getNewAlgorithm;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.controllers.ControllersTestsUtils;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.MetaDataService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Views.class)
public class ProjectControllerTest {
	
	@MockBean
	private AccountService accServ;
	
	@MockBean
	private ProjectService projServ;
	
	@MockBean
	private MetaDataService metaDataService;
	
	@MockBean
	private AlgorithmService algorithmService;

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	Views views;
	
	Account acc;
	ProjectModel project;
	
//	 @TestConfiguration
//     static class Config {
//		 @Bean
//		 public ProjectController projectController() {
//			 return new ProjectController();
//		 }
//		 
//     }
	
	@Before
	public void ensureWiring(){
		assertNotNull(views);
		assertNotNull(views.getRegister());
		
		project = ProjectModel.builder()
				.account(acc)
				.name("test")
				.build();
		
		acc = Account.builder().userName("Alex").project(project).build();
		
		project.setAccount(acc);
		
		Mockito.when(accServ.getAccountByName(acc.getUserName()))
			.thenReturn(acc);
		Mockito.when(accServ.save(acc))
			.thenReturn(acc);
		
		Mockito.when(projServ.getNew()).thenReturn(project);
//		Mockito.when(projServ.getNewAlgorithm()).thenReturn(AlgorithmService.getNewAlgorithm());
		
		Mockito.when(algorithmService.getForProject(project))
			.thenReturn(Collections.emptySet());
		
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetProjectsList() throws Exception {
		
		this.mockMvc.perform(get(ProjectController.BASE_URL))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getUserHome()))
			.andExpect(model().attributeExists(ProjectController.SESSION_Account, 
					ProjectController.MODEL_ProjectsSet, ProjectController.MODEL_NewProject,
					ProjectController.MODEL_BackBean))
			;
	}
	
	
	@Test
	@WithMockUser("Alex")
	public final void testGetNonExistingProject() throws Exception {
		Mockito.when(projServ.getByNameAndAccount(any(String.class), any(Account.class)))
			.thenReturn(Optional.empty());
		mockMvc.perform(get(RequestPath.project+"/nonextproj"))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getErrors().getNotFound()))
		;
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetProjectWithEmptyAlgorithmsSet() throws Exception {
		Mockito.when(projServ.getByNameAndAccount(any(String.class), any(Account.class)))
			.thenReturn(Optional.of(project));
		
		Set<MetaData> dataSet = new HashSet<>();
		dataSet.add(MetaData.builder().build());
		Mockito
			.when(metaDataService.getForProject(project))
			.thenReturn(dataSet);
		
		mockMvc.perform(get(ProjectController.BASE_URL + "/"+project.getName()))
			.andExpect(status().isOk())
//			.andExpect(view().name(views.getProject().getNewAlg()))
			.andExpect(view().name(views.getProject().getMain()))
			.andExpect(model().attribute(AlgorithmController.MODEL_AlgorithmsList, Collections.EMPTY_LIST))
			.andExpect(model().attribute(ProjectController.SESSION_CurrentProject, project))
			.andExpect(model().attributeExists(AlgorithmController.MODEL_NewAlgorithm))
		;
		
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetProjectWithNotEmptyAlgorithmsSet() throws Exception {
		Algorithm alg = getNewAlgorithm(null);
		alg.setName("Algorithm0");
		alg.setProject(project);
		project.getAlgorithms().add(alg);
		
		Mockito.when(projServ.getByNameAndAccount(project.getName(), acc))
			.thenReturn(Optional.of(project));
		
		mockMvc.perform(get(ProjectController.BASE_URL + "/" +project.getName()))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getProject().getMain()))
			.andExpect(model().attribute(ProjectController.SESSION_CurrentProject, project))
			// algorithm's model data
			.andExpect(model().attributeExists(AlgorithmController.MODEL_AlgorithmsList))
			.andExpect(model().attributeExists(AlgorithmController.MODEL_NewAlgorithm))
			.andExpect(model().attribute(AlgorithmController.MODEL_AlgBaseURL, 
					ControllersTestsUtils.setUriComponent(AlgorithmController.BASE_URL, project.getName())
					.toString()))
			.andExpect(model().attribute(AlgorithmController.MODEL_SelDataURL, 
					ControllersTestsUtils.setUriComponent(AlgorithmController.URL_Select_DataSet, project.getName())
					.toUriString()))
			;
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testPostDelAlgorithm() throws Exception {
		Algorithm algorithm = getNewAlgorithm(null);
		algorithm.setName("testAlg0");
		
		algorithm.setProject(project);
		project.getAlgorithms().add(algorithm);

		
		mockMvc
			.perform(post(ProjectController.URL_DeleteProject)
//						.sessionAttr("currentProject", project)
						.param("names", algorithm.getName()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(ProjectController.BASE_URL))
			
		;
			
	}
	

}
