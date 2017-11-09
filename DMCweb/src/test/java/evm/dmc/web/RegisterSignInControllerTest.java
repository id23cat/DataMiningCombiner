package evm.dmc.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.service.RequestPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
public class RegisterSignInControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Value("${views.regsign}")
	String regSignView;
	
	@Test
	public final void getRegisterTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.REGISTER))
		.andExpect(status().isOk())
		.andExpect(view().name(regSignView))
		.andExpect(model().attributeExists("registrationForm"))
		.andExpect(content().string(containsString("<li class=\"active\"><a data-toggle=\"tab\" href=\"#register\">Register</a></li>")));
	}
	
	@Test
	public final void getSignInTest() throws Exception {
		this.mockMvc.perform(get(RequestPath.SIGNIN))
		.andExpect(status().isOk())
		.andExpect(view().name(regSignView.concat(" :: regsignForm(active='signin')")))
		.andExpect(model().attributeExists("registrationForm"))
		.andExpect(content().string(containsString("<li class=\"active\"><a data-toggle=\"tab\" href=\"#signin\">Sign In</a></li>")));
	}

}
