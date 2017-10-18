package evm.dmc.web.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.DmcWebApplication;
import evm.dmc.service.testing.ViewsService;
import evm.dmc.web.testing.ShowTableController;


@RunWith(SpringRunner.class)
//@WebMvcTest(ShowTableController.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
public class ShowTableControllerTest {
	@Autowired
	ShowTableController controller;
	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testShowTable() throws Exception {		
		standaloneSetup(controller).build()
		.perform(get("/testing/showtable/table/123").accept(MediaType.TEXT_PLAIN))
		/*.andDo(print())*/
		.andExpect(status().isOk())
		.andExpect(view().name("testing/showtable"));
	}

	@Test
	public void testListBeans() throws Exception {
		standaloneSetup(controller).build()
		.perform(get("/testing/showtable/listbeans").accept(MediaType.TEXT_PLAIN))
		/*.andDo(print())*/
		.andExpect(status().isOk())
		.andExpect(view().name("testing/showtable"));
	}

}
