package evm.dmc.weka.function;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.exceptions.LoadDataException;
import weka.core.Instances;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaCSVLoadTest {
	@Autowired
	@WekaFW
	Framework frmwk;

	CSVLoader csv;

	@Value("${wekatest.datasource1}")
	String sourceFile1;

	@Value("${wekatest.datasource2}")
	String sourceFile2;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() {
		Object bck = csv;
		csv = (CSVLoader) frmwk.getDMCFunction("Weka_CSVLoader");
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

}
