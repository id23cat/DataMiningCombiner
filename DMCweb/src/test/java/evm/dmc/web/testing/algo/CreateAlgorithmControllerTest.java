package evm.dmc.web.testing.algo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.core.api.Algorithm;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "management.port=-1")
//@WebMvcTest(controllers = {CreateAlgorithmController.class}, secure = false)
@AutoConfigureMockMvc
public class CreateAlgorithmControllerTest {
		
//	@MockBean
//	Algorithm mockAlgorithm;
//	
//	@MockBean
//	@DefaultProject
//	Project mockProject;
//	
//	AlgorithmModel model;
	
	@Autowired
	MockMvc mockMvc;
	
	
//	@Before
//	public void setup() throws MalformedURLException {
//		model = new AlgorithmModel();
//		when(mockAlgorithm.getModel()).thenReturn(model);
//		when(mockAlgorithm.toString()).thenReturn("Mock Bean");
//		when(mockProject.createAlgorithm()).thenReturn(mockAlgorithm);
//		
//		algController.setProject(mockProject);
//		
//		mockMvc = standaloneSetup(algController).build();
//	}
	
	@Test
	public void testModelContent() throws Exception {
		this.mockMvc.perform(get("/testing/UID/project/12/createalg"))
			.andExpect(status().isOk())
			.andExpect(view().name("testing/createalg"))
			.andExpect(model().attributeExists("algModel"));
//			.andExpect(model().attribute("algModel", sameInstance(model)));
	}
	

}
