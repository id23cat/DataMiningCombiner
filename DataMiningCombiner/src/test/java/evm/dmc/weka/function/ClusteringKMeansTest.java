package evm.dmc.weka.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
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
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test(expected = DataOperationException.class)
	public final void testGetResultBeforeExecute() {
		Data[] trtst = data.getTrainTest(1);

		kms.getResultDataModel();
		
		thrown.expectMessage(AbstractDMCFunction.EMPTY_RESULT_EXC_MSG);
		
	}
	
	@Test
	public final void testGetResultModel() {
		Data[] trtst = data.getTrainTest(1);
		kms.train(trtst[0]);

		kms.setArgs(data);
		kms.execute();
		DataModel res = kms.getResult().getDataModel();
		System.out.println(res);
	}

}
