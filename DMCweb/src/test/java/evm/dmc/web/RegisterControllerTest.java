package evm.dmc.web;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.api.model.account.Role;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
@EnableConfigurationProperties(Views.class)
public class RegisterControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	Views views;
	
		
	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	public final void getRegisterTest() throws Exception {
		assertNotNull(views);
		assertNotNull(views.getRegister());
		
		this.mockMvc.perform(get(RequestPath.register))
		.andExpect(status().isOk())
		.andExpect(view().name(views.getRegister()))
		.andExpect(model().attributeExists("account"));
		
	}
	
}
