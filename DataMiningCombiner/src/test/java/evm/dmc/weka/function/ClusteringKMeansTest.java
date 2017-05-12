package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.data.Data;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class ClusteringKMeansTest {

	@Autowired
	ClusteringKMeans kms;

	@Autowired
	WekaCSVLoad csv;

	// @Value("#{wekaFramework.getData(Weka_Instances.getClass())}")
	WekaData data;

	@Value("${wekatest.datasource}")
	String souceFile;

	// taking from weka.propeties
	String name = "Weka_KMeansClustering";
	String description = "Executes KMeans clustering on dataset";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testPrintInfo() throws Exception {
		assertNotNull(kms);
		csv.setSource(souceFile);
		data = (WekaData) csv.get();
		assertNotNull(data);
		assertNotNull(data.getData());

		Data train, test;
		Data[] trtst = data.getTrainTest(1);
		train = trtst[0];
		test = trtst[1];

		kms.setTrainSet(train);
		kms.printInfo();

		kms.evaluate(test);

	}

	@Test
	public final void testGetName() {
		assertEquals(name, kms.getName());
	}

	@Test
	public final void testGetDescription() {
		assertEquals(description, kms.getDescription());
	}

}
