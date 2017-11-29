package evm.dmc.business.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.service.Views;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional()
@ActiveProfiles("test")
@Import(Views.class)
public class AccountRepositoryTest {
	private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryTest.class);
	
	@Autowired
    private TestEntityManager entityManager;

	@MockBean
	private AccountService mockAccService;
	
	@Autowired
    private AccountRepository repository;

	@Before
	public void init() {
		this.entityManager.persist(new AccountExt("id42cat", "password", "id42cat@tut.by", "Alex", "Demidchuk", "ADMIN").getAccount());
		this.entityManager.persist(new AccountExt("admin3", "password", "admin@mail.org", "Admin", "AD_min", "ADMIN").getAccount());
	}
	
	@Test
	public final void testFindByEmail() throws Exception {
		Account acc = this.repository.findByEmail("admin@mail.org").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Admin");
	}
	
	@Test
	public final void testFindByUserName() throws Exception {
		Account acc = this.repository.findByUserName("id42cat").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alex");
	}
	
	@Test(expected = Exception.class)
	public final void testFindByNotExistingUserName() throws Exception {
		Account acc = this.repository.findByUserName("_").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alexx");
	}

	@Test
	public final void testFindByRoleString() {
		List<Account> accCollection = this.repository.findByRole(Role.ADMIN);
		assertThat(accCollection).isNotEmpty();
		assertThat(accCollection).hasSize(2);
	}

	@Test
	public final void testFindByRoleStringPageable() {
		List<Account> accCollection = this.repository.findByRole(Role.ADMIN, new PageRequest(0,1));
		assertThat(accCollection).hasSize(1);
		assertThat(accCollection.get(0).getUserName()).isEqualTo("id42cat");
		
		accCollection = this.repository.findByRole(Role.ADMIN, new PageRequest(1,1));
		assertThat(accCollection).hasSize(1);
		assertThat(accCollection.get(0).getUserName()).isEqualTo("admin3");
	}

	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public final void testAddExistingAccount() {
		repository.save(new AccountExt("id42cat", "password", "id42cat@tut.by", "Alex", "Demidchuk", "ADMIN"));
	}
	
	@Test
	public final void testAccountManipulation() {
		{
			Account account = repository.findByUserName("id42cat").get();
			System.out.println("-= Account entities first: " + System.identityHashCode(account));
			account.setFirstName("XXX");
			Account account2 = repository.findByEmail("id42cat@tut.by").get();
			assertThat(account2.getFirstName()).isEqualTo("XXX");
			assertTrue(account == account2);
			assertTrue(account.equals(account2));
			System.out.println("-= Account entities: " + System.identityHashCode(account));
		}
		System.gc();
		Account account = repository.findByUserName("id42cat").get();
		System.out.println("-= Account entities next: " + System.identityHashCode(account));
	}
}
