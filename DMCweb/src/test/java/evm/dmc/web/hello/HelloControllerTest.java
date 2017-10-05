package evm.dmc.web.hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import evm.dmc.web.hello.HelloController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

public class HelloControllerTest {

	@Test
	public final void testGreeting() throws Exception {
		HelloController controller = new HelloController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/hello"))
		.andExpect(view().name("jsp/greeting"));
	}

}
