package evm.dmc.python.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.data.Data;
import evm.dmc.python.DMCPythonConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCPythonConfig.class)
@TestPropertySource("classpath:jeptest.properties")
public class PyReadFileTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Value("#{pythonFramework.getDMCFunction(\"Python_ReadFile\")}")
	// private DMCFunction function;
	private PyReadFile function;

	@Value("${jeptest.datafile}")
	private String dataFile;

	@Value("#{pythonFramework.getData(Python_String.getClass())}")
	private Data pyFileName;

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
		Data result = function.getResult();
		assertNotNull(result);

		Data str = function.convert(result);
		System.out.println(str.getData());
		assertFalse(systemOutRule.getLog().isEmpty());
	}

}
