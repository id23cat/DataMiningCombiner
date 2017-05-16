package evm.dmc.weka.integration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.DataFactory;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.function.WekaFunctions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class KMeansIntegrationTest {
	@Autowired
	@WekaFW
	Framework frmwk;

	@Autowired
	@WekaFW
	DataFactory datFactory;

	@Value("${wekatest.datasource}")
	String sourceFileName;

	@Test
	public final void test() {
		CSVLoader loader = (CSVLoader) frmwk.getDMCFunction("Weka_CSVLoader");
		Data data = ((WekaData) loader.setSource(sourceFileName).get()).getAttributes(6, 9, 12);
		assertNotNull(data);

		// execute Normalization
		DMCFunction norm = frmwk.getDMCFunction(WekaFunctions.NORMALIZATION);
		norm.setArgs(data);

		DMCFunction kmenas = frmwk.getDMCFunction(WekaFunctions.KMEANS);
		kmenas.setArgs(data);

	}

}
