package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
//@Rollback
//@Transactional(propagation=Propagation.REQUIRES_NEW)
@ComponentScan(basePackages = {"evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class ProjectServiceTest {
    private static final String PROJECTNAME_1 = "TestProject1";
    private static final String PROJECTNAME_2 = "TestProject2";

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
        accService.addProject(account, ProjectModel.builder()
                .account(account)
                .projectType(ProjectType.SIMPLEST_PROJECT)
                .name(PROJECTNAME_1)
                .build());
        accService.addProject(account, ProjectModel.builder()
                .account(account)
                .projectType(ProjectType.SIMPLEST_PROJECT)
                .name(PROJECTNAME_2)
                .build());
//		accService.save(account);

    }

    @Test
    public final void testGetByName() throws ProjectNotFoundException {
        assertNotNull(projectService);
        assertThat(projectService.getByName(PROJECTNAME_1).findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(PROJECTNAME_1))
                .getName(), equalTo(PROJECTNAME_1));
    }

    @Test
    public final void testGetAll() {
        long initCount = projectService.getAll().count();
        projectService.save(Optional.of(ProjectModel.builder()
                .account(account)
                .projectType(ProjectType.SIMPLEST_PROJECT)
                .name("TestTest")
                .build()));
        assertThat(projectService.getAll().count(), equalTo(initCount + 1L));

        assertThat(projectService.getAll().count(), equalTo((long) projectService.getAllAsList().size()));
    }

    @Test
    public final void testSave() {
        String name = "TestingTesting";
        ProjectModel tmpProj = ProjectModel.builder()
                .account(account)
                .projectType(ProjectType.SIMPLEST_PROJECT)
                .name(name)
                .build();

        projectService.save(Optional.of(tmpProj));

        assertThat(projectService.getByName(name).findFirst().get().getName(), equalTo(name));
    }


    @Test
    public final void testDelete() throws Exception {
        long initCount = projectService.getAll().count();

        Optional<ProjectModel> proj = projectService.getByName(PROJECTNAME_1).findFirst();
        proj.get().getAccount().getProjects().remove(proj.get());

        if (!proj.isPresent())
            throw new Exception(PROJECTNAME_1 + " not found");
        projectService.delete(proj);
        log.debug("after deletetion");

        assertThat(projectService.getAll().count(), equalTo(initCount - 1));

        List<ProjectModel> all = projectService.getAll().collect(Collectors.toList());

        assertThat(all, not(hasItem(proj.get())));


    }

}
