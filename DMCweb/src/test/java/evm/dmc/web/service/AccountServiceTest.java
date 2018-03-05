package evm.dmc.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.Role;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Rollback
@Slf4j
public class AccountServiceTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProjectService projectService;
	
	
	@Test
	public final void testInitializedEntities() {
		UserDetails user = accountService.loadUserByUsername("id23cat");
		assertThat(user.getUsername()).isEqualTo("id23cat");
		
		user = accountService.loadUserByUsername("admin");
		assertThat(user.getUsername()).isEqualTo("admin");
	}

	@Test
	public final void testSave() {
		accountService.save(new Account("user", "password2", "user@mail.org", "UUser", "Just"));
		
		UserDetails user = accountService.loadUserByUsername("user");
		
		assertThat(user.getUsername()).isEqualTo("user");
		assertThat(user.getAuthorities().stream().findFirst().get()).isEqualTo(new SimpleGrantedAuthority(Role.USER.toString()));
		
	}

	@Test
	public final void testLoadUserByUsername() {
		accountService.save(new Account("user1", "password1", "user1@mail.org", "UUser1", "Just1"));
		accountService.save(new Account("user2", "password2", "user2@mail.org", "UUser2", "Just2"));
		
		assertThat( accountService.loadUserByUsername("user1").getUsername()).isEqualTo("user1");
		assertThat( accountService.loadUserByUsername("user2").getUsername()).isEqualTo("user2");
		assertThat( accountService.loadUserByUsername("id23cat").getUsername()).isEqualTo("id23cat");
	}

	@Test(expected = UsernameNotFoundException.class )
	public final void testDelete() {
		accountService.save(new Account("user", "password2", "user@mail.org", "UUser", "Just"));
		Account acc = loadAccount("user");
		accountService.delete(acc);
		accountService.getAccountByName("user");
	}
	
	
	// Integration with ProjectService
	@Test
	public final void testProjectsSetAccess() {
		log.debug("======= begin test =====");
		
		Account acc = loadAccount("id23cat");
		assertThat(acc, notNullValue());
//		log.debug("Loaded projects: {}", projectService.getByAccount(acc).collect(Collectors.toList()));
		log.debug("Loaded projects: {}", projectService.getByAccountAsList(acc));
		log.debug("--- access to set");
		acc.getProjects().stream().count();
		assertThat(acc.getProjects(), notNullValue());
		assertThat(acc.getProjects().size(), equalTo(3));
		
		log.debug("--- remove one item");
		ProjectModel pro1 = acc.getProjects().stream()
							.filter((pro) -> pro.getName().equals("test1"))
							.findFirst().orElse(null);
		assertNotNull(pro1);
		acc.removeProject(pro1);
		pro1=null;
		assertThat(acc.getProjects().size(), equalTo(2));
		Stream<ProjectModel> proStream = projectService.getByAccount(acc);
		assertThat(proStream.count(), equalTo(2L));
		
		log.debug("======= finish test ======");
	}
	
	@Test
	public final void testAddProject() throws Exception {
		Account account = loadAccount("id23cat");
		
		ProjectModel project = new ProjectModel();
		project.setName("testProject");
		
		project = accountService.addProject(account, project);
		
		log.debug("Persisted project: {}", project);
		
		ProjectModel persistedProject = entityManager.find(ProjectModel.class, project.getId());
		
//		ProjectModel persistedProject = projectService.getByNameAndAccount(project.getName(), account).orElseThrow(Exception::new);
		
		assertThat(account.getProjects(), hasItem(persistedProject));
	}
	
	@Test
	public final void testDelProject() {
		Account account = loadAccount("id23cat");
		
		ProjectModel delProject =  account.getProjects().stream().findAny().get();
		log.debug("Project for deleteion: {}", delProject);
		
//		log.debug("Projects before deletion {}", projectService.getByAccount(account).collect(Collectors.toList()));
		accountService.delProject(account, delProject);
//		log.debug("Projects after deletion {}", projectService.getByAccount(account).collect(Collectors.toList()));
		
		
		assertFalse(accountService.findProjectByName(account, delProject.getName()).isPresent());
		
		assertThat(projectService.getByAccount(account).collect(Collectors.toList()), not(hasItem(delProject)));
		
		ProjectModel deletedProject = entityManager.find(ProjectModel.class, delProject.getId());
		assertNull(deletedProject);
		
	}
	
	@Test
	public final void testDelProjectsByNames() {
		Account account = loadAccount("id23cat");
		Set<String> names = account.getProjects().stream().map(proj -> proj.getName()).collect(Collectors.toSet());
		assertThat(projectService.getByAccount(account).count(), equalTo(3L));
		
		log.debug("Selected names: {}", names);
		
		String safeName = names.iterator().next();
		names.remove(safeName);
		
		log.debug("Set for deleteion: {}", names);
		
		accountService.delProjectsByNames(account, names);
		
		assertThat(account.getProjects().size(), equalTo(1));
		assertThat(account.getProjects().stream().findFirst().get().getName(), equalTo(safeName));
		
		assertThat(projectService.getByAccount(account).count(), equalTo(1L)); 
		assertThat(projectService.getByAccount(account).findFirst().get().getName(), equalTo(safeName)); 
		
	}
	
	private final Account loadAccount(String name) {
		return accountService.getAccountByName(name);
	}

}
