package evm.dmc.web.algo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.Project;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ComponentScan({ "evm.core", "evm.core.api", "evm.dmc.web.config"  })
@RunWith(SpringRunner.class)
@SpringBootTest
@Import({evm.dmc.web.config.DMCRootConfig.class })
//@WebMvcTest
public class CreateAlgorithmControllerTest {
	
	@Mock
	Algorithm mockAlgorithm;
	
	@Mock
	Project mockProject;
	
	@Autowired
	CreateAlgorithmController algController;
	
	AlgorithmModel model;
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		model = new AlgorithmModel();
		when(mockAlgorithm.getModel()).thenReturn(model);
		when(mockProject.createAlgorithm()).thenReturn(mockAlgorithm);
		
		algController.setProject(mockProject);
		
		mockMvc = standaloneSetup(algController).build();
		
		
	}

	@Test
	public void test() throws Exception {

//		MockMvc mockMvc = standaloneSetup(algController)
//				.setSingleView(new InternalResourceView("/WEB-INF/views/createalg.jsp")).build();
		assertThat(algController).isNotNull();
		
		
		mockMvc.perform(get("/SASHA/careatealg"))
			.andExpect(view().name("createalg"))
			.andExpect(model().attributeExists("algModel"))
			.andExpect(model().attribute("algModel", sameInstance(model)));
	}

}
