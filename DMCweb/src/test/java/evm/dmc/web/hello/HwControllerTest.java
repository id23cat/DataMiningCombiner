package evm.dmc.web.hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import evm.dmc.web.hello.HwController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(SpringJUnit4ClassRunner.class)
public class HwControllerTest {

	@Test
	public final void testGreeting() throws Exception {
		HwController controller = new HwController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/hello"))
		.andExpect(view().name("greeting"));
	}

}
