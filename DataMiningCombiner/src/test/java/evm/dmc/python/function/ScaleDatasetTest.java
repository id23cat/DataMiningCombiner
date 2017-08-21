package evm.dmc.python.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ScaleDatasetTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Value("#{pythonFramework.getDMCFunction(\"Python_ScaleDataset\")}")
	private ScaleDataset scale;

	@Autowired
	DataLoader dataLoader;

	String name = "Python_ScaleDataset";

	@Value("${jep.dm_get_scaled_desc}")
	String description;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testExecute() {
		assertNotNull(scale);
		Data data = dataLoader.getData();

		System.out.println(data.getData());
		System.out.println(scale.getCurrentEnvironment());
		// System.out.println(scale.convert(data));

		scale.setArgs(data);
		scale.execute();
		Data result = scale.getResult();
		assertNotNull(result);
		assertNotNull(result.getData());

		System.out.println(result.getData());

		System.out.println(scale.getCurrentEnvironment());

		// Data str = scale.convert(result);
		// System.out.println(str.getData());
		// assertFalse(systemOutRule.getLog().isEmpty());
	}

	@Test
	public final void testGetName() {
		assertEquals(name, scale.getName());
	}

	@Test
	public final void testGetDescription() {
		assertEquals(description, scale.getDescription());
	}

}
