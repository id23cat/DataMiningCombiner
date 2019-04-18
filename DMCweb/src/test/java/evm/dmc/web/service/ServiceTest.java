package evm.dmc.web.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;


import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
//@Rollback
//@Transactional(propagation=Propagation.REQUIRES_NEW)
@ComponentScan(basePackages = {"evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class ServiceTest {
    protected static final String PROJECTNAME_1 = "TestProject1";
    protected static final String PROJECTNAME_2 = "TestProject2";
    protected static final String PROJECT_0 = "proj0";
    protected static final String PROJECT_1_EMPTY = "proj1";
    protected static final String PROJECT_2_EMPTY = "proj2";

    protected static final String ALGORITHM_0 = "alg0";

    protected static final String DATASET_TELECOM = "telecom";
    protected static final String DATASET_IRIS = "iris";

    protected static final String USER_NAME = "devel";

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected AccountService accService;

    protected Account account;

    protected ProjectModel project_1;

    @Before
    public void init() {
//		Account account = new Account("id42cat", "password", "id42cat@mail.sm", "Alex", "Demidchuk");
        account = accService.getAccountByName(USER_NAME);
        log.debug("Account ID: {}", account.getId());
//		this.entityManager.persist(account);
        accService.addProject(account, ProjectModel.builder().name(PROJECTNAME_1).build());
        accService.addProject(account, ProjectModel.builder().name(PROJECTNAME_2).build());
//		.save(account);

        project_1 = projectService.getByNameAndAccount(PROJECT_0, account).get();

    }
}
