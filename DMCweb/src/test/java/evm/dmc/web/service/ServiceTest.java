package evm.dmc.web.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
//@Rollback
//@Transactional(propagation=Propagation.REQUIRES_NEW)
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class ServiceTest {
	protected static final String PROJECTNAME_1 = "TestProject1";
	protected static final String PROJECTNAME_2 = "TestProject2";
	
	@Autowired
	ProjectService projectService;
	
	@Autowired 
	private AccountService accService;
	
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
}
