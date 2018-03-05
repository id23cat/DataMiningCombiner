package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
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
import org.springframework.transaction.annotation.Propagation;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.SubAlgorithm;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
//@Rollback
//@Transactional(propagation=Propagation.REQUIRES_NEW)
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
	
	private static String userName = "devel";
	
	@Before
	public void init() {
//		Account account = new Account("id42cat", "password", "id42cat@mail.sm", "Alex", "Demidchuk");
		account = accService.getAccountByName(userName);
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
		Algorithm newAlgorithm = AlgorithmService.getNewAlgorithm();
		newAlgorithm.setName("newTestAlg");
		
		ProjectModel project = projectService.getByName(PROJECTNAME_1).findFirst().get();
		Long id = project.getId();
		assertNotNull(id);
		Algorithm persistedAlg = projectService.assignAlgorithm(project, newAlgorithm);
		
		assertThat(project.getId(), equalTo(id));
		assertNotNull(persistedAlg.getId());
		Algorithm assertAlg = entityManager.find(Algorithm.class, persistedAlg.getId());
		assertThat(assertAlg, equalTo(persistedAlg));
	}
	
	@Test
	public final void testDeleteAlgorithm() {
		Algorithm newAlgorithm1 = AlgorithmService.getNewAlgorithm();
		newAlgorithm1.setName("newTestAlg1");
		Algorithm newAlgorithm2 = AlgorithmService.getNewAlgorithm();
		newAlgorithm2.setName("newTestAlg2");
		Algorithm newAlgorithm3 = AlgorithmService.getNewAlgorithm();
		newAlgorithm3.setName("newTestAlg3");
		
		ProjectModel project = projectService.getByName(PROJECTNAME_1).findFirst().get();
		assertThat(project.getAlgorithms().size(), equalTo(0));
		
		projectService.assignAlgorithm(project, newAlgorithm1);
		projectService.assignAlgorithm(project, newAlgorithm2);
		projectService.assignAlgorithm(project, newAlgorithm3);
		
		assertThat(project.getAlgorithms().size(), equalTo(3));
		
		project = projectService.deleteAlgorithm(project, newAlgorithm1);
		
		assertThat(project.getAlgorithms().size(), equalTo(2));
		assertThat(project.getAlgorithms(), hasItems(newAlgorithm2, newAlgorithm3));
		
		assertNull(entityManager.find(Algorithm.class, newAlgorithm1.getId()));
		assertNotNull(entityManager.find(Algorithm.class, newAlgorithm2.getId()));
		assertNotNull(entityManager.find(Algorithm.class, newAlgorithm3.getId()));
		
	}
	
	@Test
	public final void testDeleteAlgorithmWithExistingDependentProject() {
		Algorithm newAlgorithm1 = AlgorithmService.getNewAlgorithm();
		newAlgorithm1.setName("newTestAlg1");
		Algorithm newAlgorithm2 = AlgorithmService.getNewAlgorithm();
		newAlgorithm2.setName("newTestAlg2");
		Algorithm newAlgorithm3 = AlgorithmService.getNewAlgorithm();
		newAlgorithm3.setName("newTestAlg3");
		
		ProjectModel project = projectService.getByName(PROJECTNAME_1).findFirst().get();
		ProjectModel depProject = projectService.getByName(PROJECTNAME_2).findFirst().get();
		assertThat(project.getAlgorithms().size(), equalTo(0));
		
		newAlgorithm1 = projectService.assignAlgorithm(project, newAlgorithm1);
		newAlgorithm2 = projectService.assignAlgorithm(project, newAlgorithm2);
		newAlgorithm3 = projectService.assignAlgorithm(project, newAlgorithm3);
		
		newAlgorithm1 = projectService.addAlgorithm(depProject, newAlgorithm1);
		
		assertThat(project.getAlgorithms().size(), equalTo(3));
		assertThat(depProject.getAlgorithms(), hasItem(newAlgorithm1));
		assertThat(newAlgorithm1.getParentProject(), equalTo(project));
		
		project = projectService.deleteAlgorithm(project, newAlgorithm1);
		
		assertThat(project.getAlgorithms().size(), equalTo(2));
		assertThat(project.getAlgorithms(), hasItems(newAlgorithm2, newAlgorithm3));
		assertNotNull(entityManager.find(Algorithm.class, newAlgorithm2.getId()));
		assertNotNull(entityManager.find(Algorithm.class, newAlgorithm3.getId()));
		
		assertThat(depProject.getAlgorithms(), hasItem(newAlgorithm1));
		assertThat(newAlgorithm1.getParentProject(), equalTo(depProject));
		assertThat(newAlgorithm1.getDependentProjects(), not(hasItem(depProject)));
	}
	
	@Test
	public final void testDelAlgorithmsByNames() {
		Algorithm newAlgorithm1 = AlgorithmService.getNewAlgorithm();
		newAlgorithm1.setName("newTestAlg1");
		Algorithm newAlgorithm2 = AlgorithmService.getNewAlgorithm();
		newAlgorithm2.setName("newTestAlg2");
		Algorithm newAlgorithm3 = AlgorithmService.getNewAlgorithm();
		newAlgorithm3.setName("newTestAlg3");
		
		ProjectModel project = projectService.getByName(PROJECTNAME_1).findFirst().get();
		assertThat(project.getAlgorithms().size(), equalTo(0));
		
		projectService.assignAlgorithm(project, newAlgorithm1);
		projectService.assignAlgorithm(project, newAlgorithm2);
		projectService.assignAlgorithm(project, newAlgorithm3);
		
//		projectService.save(project);
		log.debug("Alg1: {}", newAlgorithm1);
		
		assertThat(project.getAlgorithms().size(), equalTo(3));
		
		Set<String> names = project.getAlgorithms().stream()
							.map(alg -> alg.getName()).collect(Collectors.toSet());
		
		names.remove(newAlgorithm1.getName());
		
		project = projectService.deleteAlgorithms(project, names);
		
		assertThat(project.getAlgorithms().size(), equalTo(1));
		assertThat(project.getAlgorithms().stream().findFirst().get(), equalTo(newAlgorithm1));
		
		assertNull(entityManager.find(Algorithm.class, newAlgorithm2.getId()));
		assertNull(entityManager.find(Algorithm.class, newAlgorithm3.getId()));
		assertNotNull(entityManager.find(Algorithm.class, newAlgorithm1.getId()));
		
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
