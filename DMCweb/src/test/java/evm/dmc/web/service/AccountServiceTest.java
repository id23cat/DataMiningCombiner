package evm.dmc.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jfree.util.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.Role;
import evm.dmc.web.controllers.SignInController;
import evm.dmc.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
//@Rollback
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
//    DirtiesContextTestExecutionListener.class,
//   TransactionalTestExecutionListener.class,
//    DbUnitTestExecutionListener.class })
@Slf4j
public class AccountServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);
	
	@Autowired 
	private AccountService userService;
	
	@Autowired
	private ProjectService projectService;
	
	
	@Test
	@Transactional
	@Rollback
	public final void testInitializedEntities() {
		UserDetails user = userService.loadUserByUsername("id23cat");
		assertThat(user.getUsername()).isEqualTo("id23cat");
		
		user = userService.loadUserByUsername("admin");
		assertThat(user.getUsername()).isEqualTo("admin");
	}

	@Test
	@Transactional
	@Rollback
	public final void testSave() {
		userService.save(new Account("user", "password2", "user@mail.org", "UUser", "Just"));
		
		UserDetails user = userService.loadUserByUsername("user");
		
		assertThat(user.getUsername()).isEqualTo("user");
		assertThat(user.getAuthorities().stream().findFirst().get()).isEqualTo(new SimpleGrantedAuthority(Role.USER.toString()));
		
	}

	@Test
	@Transactional
	@Rollback
	public final void testLoadUserByUsername() {
		userService.save(new Account("user1", "password1", "user1@mail.org", "UUser1", "Just1"));
		userService.save(new Account("user2", "password2", "user2@mail.org", "UUser2", "Just2"));
		
		assertThat( userService.loadUserByUsername("user1").getUsername()).isEqualTo("user1");
		assertThat( userService.loadUserByUsername("user2").getUsername()).isEqualTo("user2");
		assertThat( userService.loadUserByUsername("id23cat").getUsername()).isEqualTo("id23cat");
	}

	@Test(expected = UsernameNotFoundException.class )
	@Transactional
	@Rollback
	public final void testDelete() {
		userService.save(new Account("user", "password2", "user@mail.org", "UUser", "Just"));
		Account acc = loadAccount("user");
		userService.delete(acc);
		userService.getAccountByName("user");
	}
	
	
	// Integration with ProjectService
	@Test
	@Transactional
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
	
	@Transactional(readOnly = true)
	public final Account loadAccount(String name) {
		return userService.getAccountByName(name);
	}

}
