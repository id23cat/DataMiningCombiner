package evm.dmc.business;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.AccountExt;
import evm.dmc.business.account.AccountRepository;
import evm.dmc.business.account.AccountRepositoryTest;
import evm.dmc.business.account.AccountService;
import evm.dmc.service.Views;
import evm.dmc.testannotations.TransactionalDataTest;
import evm.dmc.web.exceptions.UserNotExistsException;

@RunWith(SpringRunner.class)
@TransactionalDataTest
@Import(AccountService.class)
public class ProjectModelRepositoryTest {
	private static final Logger logger = LoggerFactory.getLogger(ProjectModelRepositoryTest.class);
	private static final String PROJECTNAME_1 = "TestProject1";
	private static final String PROJECTNAME_2 = "TestProject2";
	
	@Autowired
	private ProjectModelRepository projectRepository;
	
	@Autowired
	private AccountRepository accRepository;
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Before
	public void init() {
		Account account = new Account("id42cat", "password", "id42cat@mail.sm", "Alex", "Demidchuk");
		account.getProjects().add(new ProjectModel(ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_1));
		account.getProjects().add(new ProjectModel(ProjectType.SIMPLEST_PROJECT, null, null, PROJECTNAME_2));
		this.entityManager.persist(account);
		
	}


//	@Test
//	public final void testFindAllByAccount() {
//		Account account = accRepository.findByUserName("id42cat").get();
//		assertNotNull(account);
//		logger.debug("Account: ", account.getFirstName());
//		assertThat(projectRepository.findByAccountId(account.getId()).iterator().next().getProjectName(), 
//					anyOf(equalTo(PROJECTNAME_1), equalTo(PROJECTNAME_2)));
//	}

	@Test
	public final void testFindAll() throws UserNotExistsException {
		Account acc = accRepository.findByUserName("id42cat").orElseThrow(() -> new UserNotExistsException());
		assertThat(acc.getProjects().size(), equalTo(2));
		
//		Iterator<ProjectModel> it = projectRepository.findAll().iterator();
//		assertTrue(it.hasNext());

	}


}
