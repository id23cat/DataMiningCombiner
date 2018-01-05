package evm.dmc.web.controllers.project;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.config.SecurityConfig;
import evm.dmc.service.ProjectServiceTest;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Views.class)
@Slf4j
public class ProjectControllerTest {
	
	@MockBean
	private AccountService accServ;
	
	@MockBean
	private ProjectService projServ;

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	Views views;
	
	Account acc;
	ProjectModel proj;
	
	 @TestConfiguration
     static class Config {
		 @Bean
		 public ProjectController projectController() {
			 return new ProjectController();
		 }
		 
     }
	
	@Before
	public void ensureWiring(){
		assertNotNull(views);
		assertNotNull(views.getRegister());
		
		acc = new Account("Alex");
		proj = new ProjectModel(ProjectType.SIMPLEST_PROJECT, null, null, "test");
		acc.getProjects().add(proj);
		Mockito.when(accServ.getAccountByName(acc.getUserName()))
			.thenReturn(acc);
		Mockito.when(accServ.save(acc))
			.thenReturn(acc);
		
		Mockito.when(projServ.getNew()).thenReturn(proj);
		
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetProjectsList() throws Exception {
		
		this.mockMvc.perform(get(RequestPath.project))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getProject().getMain()))
			.andExpect(model().attributeExists("account", "projectsList", "newProject"))
			;
	}
	
//	@Test
//	@WithMockUser("Alex")
//	public final void testPostAddProject() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilderUtils.postForm(RequestPath.addProject, proj))
//				.andExpect(status().isOk());
//	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetNonExistingProject() throws Exception {
		Mockito.when(projServ.getByName(any(String.class)))
			.thenReturn(Optional.empty());
		mockMvc.perform(get(RequestPath.project+"/nonextproj"))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getErrors().getNotFound()))
		;
	}
	
	@Test
	@WithMockUser("Alex")
	public final void testGetProjectWithEmptyAlgorithmsSet() throws Exception {
		Mockito.when(projServ.getByName(proj.getProjectName()))
			.thenReturn(Optional.of(proj));
		mockMvc.perform(get(RequestPath.project+"/"+proj.getProjectName()))
			.andExpect(status().isOk())
			.andExpect(view().name(views.getProject().getNewAlg()))
		;
	}

}
