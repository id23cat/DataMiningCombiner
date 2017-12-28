package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;

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
import evm.dmc.core.api.DMCFunction;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
public class NormalizationTest {
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
		DMCFunction normal = frmwk.getDMCFunction(WekaFunctions.NORMALIZATION);
		normal.setArgs(data);
		normal.execute();

		double[] res0 = data.getAllValuesAsDoubleAt(0);
		double[] res1 = data.getAllValuesAsDoubleAt(1);

		int max0 = (int) Arrays.stream(res0).max().getAsDouble();
		int max1 = (int) Arrays.stream(res1).max().getAsDouble();

		int min0 = (int) Arrays.stream(res0).min().getAsDouble();
		int min1 = (int) Arrays.stream(res1).min().getAsDouble();

		assertEquals(max0, 1);
		assertEquals(max1, 1);

		assertEquals(min0, 0);
		assertEquals(min1, 0);

	}

}
