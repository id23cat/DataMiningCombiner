package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.api.Data;
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

	String name = WekaFunctions.KMEANS;
	String description = "Executes KMeans clustering on dataset";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() {
		csv.setSource(souceFile);
		data = (WekaData) csv.get();
		assertNotNull(data);
		assertNotNull(data.getData());
	}

	@Test
	public final void testPrintInfo() throws Exception {
		assertNotNull(kms);
		Data train, test;
		Data[] trtst = data.getTrainTest(1);
		train = trtst[0];
		test = trtst[1];

		// kms.setTrainSet(train);
		kms.train(train);
		kms.printInfo();

		kms.evaluate(test);
		kms.printInfo();

	}

	@Test
	public final void testExecute() {
		Data[] trtst = data.getTrainTest(1);
		kms.train(trtst[0]);

		kms.setArgs(data);
		kms.execute();
		WekaData res = (WekaData) kms.getResult();
		assertEquals(data.getData().numAttributes() + 1, res.getData().numAttributes());
		System.out.println(res.getData().toString());

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(res.getData().toSummaryString());
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
