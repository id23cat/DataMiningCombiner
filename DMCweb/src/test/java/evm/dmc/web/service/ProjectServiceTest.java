package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.h2.jdbc.JdbcSQLException;
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

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
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
	
	Account account;
	
	@Before
	public void init() {
//		Account account = new Account("id42cat", "password", "id42cat@mail.sm", "Alex", "Demidchuk");
		account = accService.getAccountByName("idcat");
		log.debug("Account ID: {}", account.getId());
		account.addProject(projectService.getNew(account, ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_1));
		account.addProject(projectService.getNew(account, ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_2));
		log.debug("Trying to persist");
//		this.entityManager.persist(account);
		accService.save(account);
		
	}

	@Test
	public final void testGetByName() throws ProjectNotFoundException {
		assertNotNull(projectService);
		assertThat(projectService.getByName(PROJECTNAME_1).findFirst()
				.orElseThrow(() -> new ProjectNotFoundException())
				.getName(), equalTo(PROJECTNAME_1));
	}
	
	@Test
	public final void testGetAll() {
		long initCount = projectService.getAll().count();
		projectService.save(Optional.of(projectService.getNew(account, ProjectType.SIMPLEST_PROJECT, null, null, "TestTest")));
		assertThat(projectService.getAll().count(), equalTo(initCount + 1L));
		
		assertThat(projectService.getAll().count(), equalTo((long)projectService.getAllAsList().size()));
	}
	
	@Test
	public final void testSave() {
		String name = "TestingTesting";
		ProjectModel tmpProj = projectService.getNew(account, ProjectType.SIMPLEST_PROJECT, null, null, name);

		projectService.save(Optional.of(tmpProj));
		
		assertThat(projectService.getByName(name).findFirst().get().getName(), equalTo(name));
	}
	
	@Test
	public final void testAssignAlgorithm() {
		AlgorithmModel newAlgorithm = new AlgorithmModel();
		newAlgorithm.setName("newTestAlg");
		
		AlgorithmModel persistedAlg = projectService
				.assignAlgorithm(projectService.getByName(PROJECTNAME_1).findFirst().get(), newAlgorithm);
		
		AlgorithmModel assertAlg = entityManager.find(AlgorithmModel.class, persistedAlg.getId());
		assertThat(assertAlg, equalTo(persistedAlg));
	}
	
	@Test
	public final void testDelAlgorithmsByNames() {
		AlgorithmModel newAlgorithm1 = new AlgorithmModel();
		newAlgorithm1.setName("newTestAlg1");
		AlgorithmModel newAlgorithm2 = new AlgorithmModel();
		newAlgorithm2.setName("newTestAlg2");
		AlgorithmModel newAlgorithm3 = new AlgorithmModel();
		newAlgorithm3.setName("newTestAlg3");
		
		ProjectModel project = projectService.getByName(PROJECTNAME_1).findFirst().get();
		project.assignAlgorithm(newAlgorithm1);
		project.assignAlgorithm(newAlgorithm2);
		project.assignAlgorithm(newAlgorithm3);
		
		projectService.save(project);
		
		List<String> names = project.getAlgorithms().stream()
							.map(alg -> alg.getName()).collect(Collectors.toList());
		
		String safeName = names.remove(0);
		
		projectService.delAlgorithmsByNames(project, names.toArray(new String[0]));
		
		assertThat(project.getAlgorithms().size(), equalTo(1));
		assertThat(project.getAlgorithms().stream().findFirst().get().getName(), equalTo(safeName));
		
//		assertThat(projectService.getByAccount(account).count(), equalTo(1L)); 
//		assertThat(projectService.getByAccount(account).findFirst().get().getName(), equalTo(safeName)); 
		
	}
	
	@Test
	public final void testDelete() throws Exception {
		long initCount = projectService.getAll().count();
		
		Optional<ProjectModel> proj = projectService.getByName(PROJECTNAME_1).findFirst();
		proj.get().getAccount().getProjects().remove(proj.get());
		
		if(!proj.isPresent())
			throw new Exception(PROJECTNAME_1 + " not found");
		projectService.delete(proj);
		log.debug("after deletetion");
		
		assertThat(projectService.getAll().count(), equalTo(initCount-1));
		
		List<ProjectModel> all = projectService.getAll().collect(Collectors.toList());
		
		assertThat(all, not(hasItem(proj.get())));
		
		
	}

}
