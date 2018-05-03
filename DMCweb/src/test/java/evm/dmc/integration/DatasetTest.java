package evm.dmc.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.DmcWebApplication;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.controllers.project.ProjectController;
import evm.dmc.web.controllers.project.utils.DatasetTestUtils;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.Views;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		properties = {"management.port=-1", "security.headers.frame=true"},
		classes = DmcWebApplication.class)
@ActiveProfiles({"test", "devH2" })
//@TestPropertySource(
//		  locations = "classpath:application-integrationtest.properties")
@AutoConfigureMockMvc
public class DatasetTest {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private Views views;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private AccountService accountService;

	@Test
	@PreAuthorize("authenticated")
	@WithMockUser("id23cat")
	public final void test() throws Exception {
		Account account = accountService.getAccountByName(DatasetTestUtils.TEST_USER_NAME);
		ProjectModel project = projectService.getByNameAndAccount(DatasetTestUtils.TEST_PROJ_NAME, account).orElseThrow(()->
			new ProjectNotFoundException(DatasetTestUtils.TEST_PROJ_NAME));
		
		this.mockMvc.perform(get("/project/proj0/dataset/telecom")
				.sessionAttr(ProjectController.SESSION_CurrentProject,project))
		.andExpect(status().isOk())
		.andExpect(view().name(views.project.data.dataSource))
		;
	}

}
