package evm.dmc.rest.controllers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.DmcWebApplication;
import evm.dmc.api.model.account.Account;
import evm.dmc.config.ApplicationConfiguration;
import evm.dmc.config.MvcConfig;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.web.service.AccountService;
import evm.dmc.web.service.Views;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DmcWebApplication.class, ApplicationConfiguration.class, MvcConfig.class, Views.class},
	webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
//@DataJpaTest
@AutoConfigureMockMvc
//@EnableConfigurationProperties(Views.class)
//@WebAppConfiguration
public class RestAccountsControllerTest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	private List<Account> accountsList = new ArrayList<>();
	
	@Autowired
	private MockMvc mockMvc;
	
//	@Autowired
//	private AccountRepository accRepo;
	
	@MockBean
	private AccountService accService;
	
	@Before
	@Transactional(readOnly=true)
	public void setup() {
//		accountsList = accRepo.streamAll().collect(Collectors.toList());
		accountsList.add(Account.builder().id(0L).userName("user0").firstName("User0").build());
		accountsList.add(Account.builder().id(1L).userName("user1").firstName("User1").build());
	}

	@Test
	public final void testGetAccountsList() throws Exception {
		Mockito
			.when(accService.getAll())
			.thenReturn(accountsList.stream());
		
		mockMvc.perform(get(RestAccountsController.BASE_URL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
		;
	}

}
