package evm.dmc.weka.function;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class StandartizationTest {

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

}
