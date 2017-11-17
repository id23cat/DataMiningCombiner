package evm.dmc.business.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.web.RegisterSignInController;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class AccountServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(RegisterSignInController.class);
	
	@Autowired 
	private AccountService userService;
	
	
	@Test
	public final void testInitializedEntities() {
		UserDetails user = userService.loadUserByUsername("id23cat");
		assertThat(user.getUsername()).isEqualTo("id23cat");
		
		user = userService.loadUserByUsername("admin");
		assertThat(user.getUsername()).isEqualTo("admin");
	}

	@Test
	public final void testSave() {
		userService.save(new Account("user", "password2", "user@mail.org", "UUser", "Just"));
		
		UserDetails user = userService.loadUserByUsername("user");
		
		assertThat(user.getUsername()).isEqualTo("user");
		assertThat(user.getAuthorities().stream().findFirst().get()).isEqualTo(new SimpleGrantedAuthority("ROLE_USER"));
		
	}

	@Test
	public final void testLoadUserByUsername() {
		userService.save(new Account("user1", "password1", "user1@mail.org", "UUser1", "Just1"));
		userService.save(new Account("user2", "password2", "user2@mail.org", "UUser2", "Just2"));
		
		assertThat( userService.loadUserByUsername("user1").getUsername()).isEqualTo("user1");
		assertThat( userService.loadUserByUsername("user2").getUsername()).isEqualTo("user2");
		assertThat( userService.loadUserByUsername("id23cat").getUsername()).isEqualTo("id23cat");
	}



}
