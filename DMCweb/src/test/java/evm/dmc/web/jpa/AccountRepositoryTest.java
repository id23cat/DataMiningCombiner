package evm.dmc.web.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;

import evm.dmc.business.account.Account;
import evm.dmc.business.account.AccountRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AccountRepositoryTest {
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
    private AccountRepository repository;

	@Test
	public final void persistingTest() throws Exception {
		this.entityManager.persist(new Account("id23cat@tut.by", "id233cat", "password", "Alex", "Demidchuk", "ROLE_ADMIN"));
		Account acc = this.repository.findByUserName("id233cat").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alex");
	}
	
	@Test(expected = Exception.class)
	public final void requestNonExistingUserTest() throws Exception {
		this.entityManager.persist(new Account("id23cat@tut.by", "id233cat", "password", "Alex", "Demidchuk", "ROLE_ADMIN"));
		Account acc = this.repository.findByUserName("id23cat").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alex");
	}

}
