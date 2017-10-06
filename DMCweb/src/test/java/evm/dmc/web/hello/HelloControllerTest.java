package evm.dmc.web.hello;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import evm.dmc.service.ViewsService;
import evm.dmc.web.hello.HelloController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HelloController.class, secure = false)
public class HelloControllerTest {
	@Autowired
	private MockMvc mvc;
	

	@Test
	public final void testGreeting() throws Exception {
		mvc.perform(get("/hello")).andExpect(view().name("hello"));
	}

}
