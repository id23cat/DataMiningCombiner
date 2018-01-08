package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
//@Transactional
@ActiveProfiles({"test", "devH2"})
@Rollback
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class ProjectServiceTest {
	private static final String PROJECTNAME_1 = "TestProject1";
	private static final String PROJECTNAME_2 = "TestProject2";
	
	@Autowired
	ProjectService projectService;
	
	@Autowired 
	private AccountService accService;
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Before
	public void init() {
//		Account account = new Account("id42cat", "password", "id42cat@mail.sm", "Alex", "Demidchuk");
		Account account = accService.getAccountByName("idcat");
		account.getProjects().add(projectService.getNew(ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_1));
		account.getProjects().add(projectService.getNew(ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_2));
		this.entityManager.persist(account);
		
	}

	@Test
	public final void testGetByName() throws ProjectNotFoundException {
		assertNotNull(projectService);
		assertThat(projectService.getByName(PROJECTNAME_1)
				.orElseThrow(() -> new ProjectNotFoundException())
				.getName(), equalTo(PROJECTNAME_1));
	}
	
	@Test
	public final void testGetAll() {
		assertThat(projectService.getAll().count(), equalTo(2L));
		projectService.save(Optional.of(projectService.getNew(ProjectType.SIMPLEST_PROJECT, null, null, "TestTest")));
		assertThat(projectService.getAll().count(), equalTo(3L));
	}
	
	@Test
	public final void testSave() {
		String name = "TestingTesting";
		ProjectModel tmpProj = projectService.getNew(ProjectType.SIMPLEST_PROJECT, null, null, name);
		projectService.save(Optional.of(tmpProj));
		
		assertThat(projectService.getByName(name).get().getName(), equalTo(name));
	}
	
	@Test
	@Transactional
	@Rollback
	// TODO: not working correctly on deletion
	@Ignore
	public final void testDelete() {
		assertThat(projectService.getAll().count(), equalTo(2L));
//		projectService.delete(projectService.getNew(ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_1));
		
		projectService.delete(projectService.getByName(PROJECTNAME_1));
		projectService.delete(PROJECTNAME_1);
		entityManager.flush();
		
		assertThat(projectService.getAll().count(), equalTo(1L));
		
		List<ProjectModel> all = projectService.getAll().collect(Collectors.toList());
		projectService.save(projectService.getByName(PROJECTNAME_2));
		log.debug("=== List {}", Arrays.toString(all.toArray()));
		assertThat(all.get(0).getName(), equalTo(PROJECTNAME_2));
		
		
	}

}
