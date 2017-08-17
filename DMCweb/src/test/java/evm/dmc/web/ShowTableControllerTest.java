package evm.dmc.web;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import evm.dmc.weka.DMCWekaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@ComponentScan({ "evm.dmc.core", "evm.dmc.web", "evm.dmc.web.config"  })
@Import({evm.dmc.core.DMCCoreConfig.class })
@WebMvcTest
public class ShowTableControllerTest {
	@Autowired
	ShowTableController controller;

	@Test
	public void testShowTable() throws Exception {		
		standaloneSetup(controller).build()
		.perform(get("/showtable/table/123").accept(MediaType.TEXT_PLAIN))
		/*.andDo(print())*/
		.andExpect(status().isOk())
		.andExpect(view().name("showtable"));
	}

	@Test
	public void testListBeans() throws Exception {
		standaloneSetup(controller).build()
		.perform(get("/showtable/listbeans").accept(MediaType.TEXT_PLAIN))
		/*.andDo(print())*/
		.andExpect(status().isOk())
		.andExpect(view().name("showtable"));
	}

}