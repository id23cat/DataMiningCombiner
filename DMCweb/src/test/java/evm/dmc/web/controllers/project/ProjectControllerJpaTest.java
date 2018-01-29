package evm.dmc.web.controllers.project;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.config.WebFlowConfig;
import evm.dmc.web.controllers.CheckboxBean;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
//@Transactional
@Rollback
//@ComponentScan( basePackages = { "evm.dmc.web.controllers.project", "evm.dmc.web.service", "evm.dmc.model"}, 
//	excludeFilters = {@Filter(type = FilterType.REGEX, pattern = "evm.dmc.web.testing.*"),
//					@Filter(type = FilterType.REGEX, pattern = "evm.dmc.service.testing.*")
//	})
//@ComponentScan(basePackageClasses = {evm.dmc.web.service.AccountService.class, evm.dmc.web.controllers.project.ProjectController.class}, lazyInit = true)
//@EnableAutoConfiguration(exclude = evm.dmc.config.WebFlowConfig.class)
@ComponentScan( basePackages = { "evm.dmc.web.controllers.project", "evm.dmc.web.service", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
//@ContextConfiguration(classes = TestJpaConfig.class)
@Slf4j
public class ProjectControllerJpaTest {

//	@Autowired
	ProjectController controller;
	
	@Autowired
	AccountService accService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	Views views;
	
//	@MockBean
//	Views.Project viewProj;
//	
//	@MockBean
//	Views.Errors viewErr;
	
	@MockBean
	Model model;
	
	@MockBean 
	BindingResult bindingResult;
	
	@MockBean
	RedirectAttributes ra;
	
	@PostConstruct
	public void postConstruct() {
//		Mockito.when(viewProj.getMain()).thenReturn("main");
//		Mockito.when(viewProj.getAlgorithmsList()).thenReturn("algList");
//		Mockito.when(viewErr.getNotFound()).thenReturn("NotFound");
//		Mockito.when(views.getProject()).thenReturn(viewProj);
		

		this.controller = new ProjectController(accService, projectService, views);
	}
	
	@Before
	public void init() {
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
	}
	
	@TestConfiguration
	static class Config {
		 @Bean
		 public ViewResolver projectController() {
			 return new ThymeleafViewResolver();
		 }
	}
	
	private static final String PROJ_0 = "test0";
	private static final String PROJ_1 = "test1";
	private static final String PROJ_2 = "test2";

	

	@Test
	@Transactional
	public final void testPostAddAlgorithm() {
		Account account = accService.getAccountByName("idcat");
		ProjectModel project = projectService.getByNameAndAccount(PROJ_0, account).get();
		AlgorithmModel newAlg = new AlgorithmModel();
		newAlg.setName("testAlg0");
		
		controller.postAssignAlgorithm(project, newAlg);
		
		assertThat(project.getAlgorithms(), hasItem(newAlg));
		
	}
	
	@Test
	@Rollback
	public final void testPostDelAlgorithm() {
		Account account = accService.getAccountByName("idcat");
		ProjectModel removeProj = projectService.getByNameAndAccount(PROJ_0, account).get();
		CheckboxBean bean = new CheckboxBean();
		
	}
	

}
