package evm.dmc.weka.function;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
public class StandardizationTest {

	@Autowired
	WekaCSVLoad loader;

	@Autowired
	WekaFramework frmwk;

	@Value("${wekatest.datasourceSmall}")
	String file;

	WekaData data;

	@Before
	public void init() {
		data = (WekaData) loader.setSource(file).get();
	}

	@Test
	public final void testExecute() {
		DMCFunction normal = frmwk.getDMCFunction(WekaFunctions.STANDARDIZATION);
		normal.setArgs(data);
		normal.execute();

		double[] res0 = data.getAllValuesAsDoubleAt(0);
		double[] res1 = data.getAllValuesAsDoubleAt(1);

		double max0 = Arrays.stream(res0).max().getAsDouble();
		double max1 = Arrays.stream(res1).max().getAsDouble();

		double min0 = Arrays.stream(res0).min().getAsDouble();
		double min1 = Arrays.stream(res1).min().getAsDouble();

		assertThat(max0, closeTo(1.916, 0.01));
		assertThat(max1, closeTo(1.922, 0.01));

		assertThat(min0, closeTo(-1.465, 0.01));
		assertThat(min1, closeTo(-1.223, 0.01));
	}
	
	@Test
	public final void testGettingModel() {
		DMCFunction stand1 = frmwk.getDMCFunction(WekaFunctions.STANDARDIZATION);
		FunctionModel model1 = stand1.getFunctionModel();
		assertNotNull(model1);
		assertTrue(model1.getProperties().isEmpty());
		
		// assert that different exemplars has different model exemplars		
		DMCFunction stand2 = frmwk.getDMCFunction(WekaFunctions.STANDARDIZATION);
		FunctionModel model2 = stand2.getFunctionModel();
		model1.getProperties().put("key", "value");
		assertFalse(stand1 == stand2);
		assertFalse(model1.getProperties().isEmpty());
		assertTrue(model2.getProperties().isEmpty());
		assertTrue(model1.getFramework().equals(model2.getFramework()));
		assertEquals(model2.getName(), WekaFunctions.STANDARDIZATION);
	}

}
