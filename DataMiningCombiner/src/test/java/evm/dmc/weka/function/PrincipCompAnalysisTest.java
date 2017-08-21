package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class PrincipCompAnalysisTest {
	@Autowired
	WekaCSVLoad loader;

	@Autowired
	WekaFramework frmwk;

	@Value("${wekatest.datatelecom}")
	String file;

	Data data;

	@Before
	public void init() {
		WekaData dta = (WekaData) loader.setSource(file).get();
		data = dta.getAttributes(6, 9, 12);
	}

	@Test
	public final void testExecute() {
		DMCFunction pca = frmwk.getDMCFunction(WekaFunctions.PCA);
		pca.setArgs(data);
		pca.execute();

		WekaData res = (WekaData) pca.getResult();

		assertEquals(2, res.getData().numAttributes());

	}

}
