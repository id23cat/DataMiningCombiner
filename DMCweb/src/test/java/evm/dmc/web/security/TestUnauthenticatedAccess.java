package evm.dmc.web.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.service.RequestPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
public class TestUnauthenticatedAccess {
	@Autowired
	MockMvc mockMvc;
	
	@Value("${views.signin}")
	String signinView;
	
	@Value("${views.register}")
	String registerView;
	
	@Value("${views.index}")
	String indexView;
	
	@Test
	public void getIndexUnauthenticatedTest() throws Exception {
		mockMvc
		.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name(indexView))
		;
	}
	
	@Test
	public void getRegisterUnauthenticatedTest() throws Exception {
		mockMvc
		.perform(get(RequestPath.REGISTER))
		.andExpect(status().isOk())
		.andExpect(view().name(registerView))
		;
	}
	
	@Test
	public void getSignInUnauthenticatedTest() throws Exception {
		mockMvc
		.perform(get(RequestPath.SIGNIN))
		.andExpect(status().isOk())
		.andExpect(view().name(signinView))
		;
	}
	
	@Test
	public final void getUserHomeUnauthenticatedTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.USER_HOME))
		.andExpect(status().is3xxRedirection())		// login form redirection
		.andExpect(redirectedUrlPattern("**/" + signinView))
		;
		
	}

}
