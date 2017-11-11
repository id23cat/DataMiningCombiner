package evm.dmc.web.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.service.RequestPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
public class TestAuthenticatedAccess {
	@Autowired
	MockMvc mockMvc;
	
	@Value("${views.userHome}")
	String userHomeView;
	
	@Value("${views.adminHome}")
	String adminHomeView;
	
	@Test
	@WithMockUser
	public final void getUserHomeAuthenticatedTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.USERHOME))
		.andExpect(status().isOk())		
		.andExpect(view().name(userHomeView))
		;
	}
	
	@Test
	@WithMockUser
	public final void getAdminHomeAuthenticated_NotAuthorizedTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.ADMINHOME))
		.andExpect(status().isForbidden())	
		;
	}
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public final void getAdminHomeAuthenticated_AndAuthorizedTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.ADMINHOME))
		.andExpect(status().isOk())	
		;
	}
	
}
