package evm.dmc.python.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.api.Data;
import evm.dmc.python.DMCPythonConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCPythonConfig.class)
@TestPropertySource({ "classpath:jeptest.properties", "classpath:jep.properties" })
@Ignore("Fails when using JEP")
public class PyReadFileTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Value("#{pythonFramework.getDMCFunction(\"Python_ReadFile\")}")
	private PyReadFile function;

	@Value("${jeptest.datafile}")
	private String dataFile;

	@Value("#{pythonFramework.getData(Python_String.getClass())}")
	private Data pyFileName;

	String name = "Python_ReadFile";

	@Value("${jep.readFileCSV_desc}")
	String desc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@Test
	public final void testPyReadFile() {
		assertNotNull(function);
		assertNotNull(pyFileName);
		pyFileName.setData(dataFile);
		function.setArgs(pyFileName);

		function.execute();
		System.out.println(function.getCurrentEnvironment());

		Data result = function.getResult();
		assertNotNull(result);
		assertNotNull(result.getData());

		// Data str = function.convert(result);
		// System.out.println(str.getData());
		// assertFalse(systemOutRule.getLog().isEmpty());
	}

	@Test
	public final void testGetName() {
		assertEquals(name, function.getName());
	}

	@Test
	public final void testGetDescription() {
		assertEquals(desc, function.getDescription());
	}

}
