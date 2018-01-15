package evm.dmc.web.controllers.project;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.controllers.CheckboxBean;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
//@Transactional
@Rollback
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class ProjectControllerJpaTest {
	@Autowired
	ProjectController controller;
	
	@Autowired
	AccountService accService;
	
	@Autowired
	ProjectService projectService;
	
	@MockBean
	Model model;
	
	@MockBean 
	BindingResult bindingResult;
	
	@MockBean
	RedirectAttributes ra;
	
	@Before
	public void init() {
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
	}
	
	private static final String PROJ_0 = "test0";
	private static final String PROJ_1 = "test1";
	private static final String PROJ_2 = "test2";

	@Test
	@Transactional
	public final void testPostAddProject() {
		final String NEW_PROJ = "Unit0";
		Account account = accService.getAccountByName("idcat");
		
		for(ProjectModel proj : account.getProjects()) {
			assertThat(proj.getName(), anyOf(equalTo(PROJ_0), equalTo(PROJ_1), equalTo(PROJ_2)));
		}
		
		ProjectModel newProj = new ProjectModel();
		newProj.setName(NEW_PROJ);
		
		controller.postAddProject(account, newProj, bindingResult, ra);
		
		account = accService.merge(account);
		assertThat(NEW_PROJ, anyOf(
				account.getProjects()
				.stream()
				.map((prj)->equalTo(prj.getName()))
				.collect(Collectors.toList())));
		
		Optional<ProjectModel> proj =  projectService.getByNameAndAccount(NEW_PROJ, account);
		assertTrue(proj.isPresent());
		assertThat(proj.get().getAccount(), equalTo(account));
	}

	@Test
//	@Transactional
	@Rollback
	public final void testPostDelProjedct() {
		Account account = accService.getAccountByName("idcat");
		ProjectModel removeProj = projectService.getByNameAndAccount(PROJ_0, account).get();
		CheckboxBean bean = new CheckboxBean();
		bean.setNames(Arrays.array(PROJ_0));
		
		controller.postDelProjedct(account, bean, bindingResult, ra);
//		controller.postDelProjedct(account, account.getProjects(), bean, bindingResult, ra);
		
		assertThat(projectService.getAll().collect(Collectors.toList()), not(hasItem(removeProj)));
		assertThat(account.getProjects(), not(hasItem(removeProj)));
	}

}
