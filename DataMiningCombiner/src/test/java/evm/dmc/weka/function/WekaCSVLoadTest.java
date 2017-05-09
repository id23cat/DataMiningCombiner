package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.weka.DMCWekaConfig;
import weka.core.Instances;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaCSVLoadTest {
	@Autowired
	CSVLoader csv;

	String souceFile = "Data/telecom_churn.csv";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testGet() {
		assertNotNull(csv);
		csv.setSource(souceFile);
		Data data = csv.get();
		assertNotNull(data.getData());
		Instances inst = (Instances) data.getData();
		int length = inst.numInstances();
		assertEquals(3333, length);

	}

}
