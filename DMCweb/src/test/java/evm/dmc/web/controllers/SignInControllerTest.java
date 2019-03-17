package evm.dmc.web.controllers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
@AutoConfigureMockMvc
public class SignInControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${views.regsign}")
    String regSignView;

    @Value("${views.signin}")
    String signinView;


    @Autowired
    Views views;

    @Before
    public void ensureWiring() {
        assertNotNull(views);
        assertNotNull(views.getRegister());
    }

    @Test
    public final void getSignInTest() throws Exception {
        this.mockMvc.perform(get(RequestPath.signin))
                .andExpect(status().isOk())
                .andExpect(view().name(signinView));
    }

    @Test
    public final void getSignInXreqTest() throws Exception {
        this.mockMvc.perform(get(RequestPath.signin).header("X-Requested-With", "XMLHttpRequest"))
                .andExpect(status().isOk())
                .andExpect(view().name(signinView.concat(String.format(views.getFragments().getSignin(), "signin"))))
                .andExpect(model().attributeExists("registrationForm"));
//		.andExpect(content().string(containsString("<li class=\"active\"><a data-toggle=\"tab\" href=\"#signin\">Sign In</a></li>")));
    }


}
