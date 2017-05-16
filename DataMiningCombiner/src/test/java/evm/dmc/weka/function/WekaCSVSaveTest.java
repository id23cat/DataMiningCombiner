package evm.dmc.weka.function;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.StringData;
import evm.dmc.core.exceptions.StoringException;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.WekaFramework;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaCSVSaveTest {
	@Autowired
	@WekaFW
	WekaFramework frmwk;

	WekaCSVSave csv;

	@Value("${wekatest.datasource}")
	String sourceFile;
	Data data;

	String destFile = "temp.csv";
	File tmpFile;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() throws IOException {
		csv = (WekaCSVSave) frmwk.getDMCFunction(WekaFunctions.CSVSAVER);
		assertNotNull(csv);
		tmpFile = folder.newFile(destFile);

		// TODO
		// potentially contains fails
		CSVLoader load = (CSVLoader) frmwk.getDMCFunction("Weka_CSVLoader");

		load.setSource(sourceFile);
		data = load.get();
		assertNotNull(data);
		assertNotNull(data.getData());

	}

	@Test
	public final void testSave() throws ClassCastException, StoringException {
		csv.setDestination(tmpFile);
		csv.save(data);
		assertTrue(tmpFile.length() != 0);

	}

	@Test(expected = StoringException.class)
	public final void testSavigWithoutDestination() throws StoringException {
		csv.save(data);

	}

	@Test(expected = ClassCastException.class)
	public final void testSavingUnsupportedDataType() throws ClassCastException, StoringException {
		csv.setDestination(tmpFile);
		Data<String> badData = frmwk.getData(StringData.class);
		badData.setData("Hello, World");
		csv.save(badData);
	}

}
