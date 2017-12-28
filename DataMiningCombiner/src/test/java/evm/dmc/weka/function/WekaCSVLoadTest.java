package evm.dmc.weka.function;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
import evm.dmc.core.DMCCoreConfig;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.exceptions.LoadDataException;
import evm.dmc.core.services.FrameworksService;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.exceptions.LoadHeaderException;
import weka.core.Instances;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
public class WekaCSVLoadTest {
	@Autowired
	@WekaFW
	Framework frmwk;

	WekaCSVLoad csv;

	// Telecom dataset, 3333 records
	@Value("${wekatest.datasource1}")
	String sourceFile1;

	// iris dataset
	@Value("${wekatest.datasource2}")
	String sourceFile2;

	@Value("${wekatest.datasourceDate}")
	String sourceFileDate;

	@Value("${weka.csvload_desc}")
	String description;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() {
		Object bck = csv;
		csv = (WekaCSVLoad) frmwk.getDMCFunction(WekaFunctions.CSVLOADER);
		assertNotEquals(csv, bck);
		assertNotNull(csv);
		assertThat(sourceFile1, startsWith("Data"));
		csv.setSource(sourceFile1);
	}

	@Test
	public final void testGet() {
		Data data = csv.get();
		assertNotNull(data.getData());
		Instances inst = (Instances) data.getData();
		int length = inst.numInstances();
		assertEquals(3333, length);

	}

	@Test(expected = LoadDataException.class)
	public final void testGetWithoutPervSetFileName() throws LoadDataException {
		csv.setSource("@@@");
		assertNull(csv.get());
	}

	@Test
	public final void testSequentialGettingOnDiffSource() {
		Data data1 = csv.get();
		csv.setSource(sourceFile2);
		Data data2 = csv.get();
		assertNotEquals(data1, data2);

	}

	@Test
	public final void testGetNameDescription() {
		assertEquals(WekaFunctions.CSVLOADER, csv.getName());
		assertEquals(description, csv.getDescription());

	}

	@Test
	public final void testAsDate() {
		csv.setSource(sourceFileDate);
		csv.setDateFormat("dd.MM.yyyy");
		WekaData data = getData(csv);
		data.printDebug();
		assertTrue(data.isNominal(2));
		assertTrue(data.isNominal(6));

		csv.restart();

		csv.asDate(2);
		csv.asDate(6);
		data = getData(csv);

		data.printDebug();
		assertTrue(data.isDate(2));
		assertTrue(data.isDate(6));
	}

	@Test
	public final void testAsNominal() {
		csv.hasHeader(false);
		csv.setSource(sourceFileDate);
		WekaData data = getData(csv);
		data.printDebug();
		assertTrue(data.isNumeric(1));
		assertTrue(data.isNumeric(3));

		csv.restart();

		csv.asNominal(1);
		csv.asNominal(3);
		data = getData(csv);

		data.printDebug();
		assertTrue(data.isNominal(1));
		assertTrue(data.isNominal(3));
	}

	@Test
	public final void testAsString() {
		csv.hasHeader(false);
		csv.setSource(sourceFileDate);
		WekaData data = getData(csv);
		data.printDebug();
		assertTrue(data.isNumeric(1));
		assertTrue(data.isNumeric(3));

		csv.restart();

		csv.asString(1);
		csv.asString(3);
		data = getData(csv);

		data.printDebug();
		assertTrue(data.isString(1));
		assertTrue(data.isString(3));
	}

	private WekaData getData(WekaCSVLoad csv) {
		WekaData data = null;
		for (int count = 0; count < 2; count++) {
			try {
				System.out.println(csv.getHead());
				data = (WekaData) csv.get();
			} catch (LoadDataException e) {
				if (!csv.getHasHeader())
					throw e;
				// probably csv file hasn't header
				System.err.println(e.getMessage());
				if (e instanceof LoadHeaderException) {
					csv.hasHeader(false);
				}

			}
		}
		return data;
	}

}
