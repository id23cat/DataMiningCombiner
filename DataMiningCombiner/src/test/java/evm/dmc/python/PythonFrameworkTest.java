package evm.dmc.python;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import evm.dmc.core.DataFactory;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.python.data.PyString;
import jep.Jep;
import jep.JepException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCPythonConfig.class)
@TestPropertySource("classpath:jeptest.properties")
public class PythonFrameworkTest {
	@Autowired
	private Jep jep;
	@Autowired
	@PythonFW
	private Framework framework;

	@Autowired
	@PythonFW
	private DataFactory dBuilder;

	@Value("${jep.scriptsFolder}")
	private String scriptsFolder;

	@Value("${jep.execFile}")
	private String execute;

	@Value("${jeptest.datafile}")
	private String dataFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@Test
	public final void testAutowiring() {
		assertNotNull(jep);
		assertNotNull(framework);

		assertNotNull(scriptsFolder);
		assertNotEquals("${jep.scriptsFolder}", scriptsFolder);
		assertNotNull(execute);
		assertNotEquals("${jep.execFile}", execute);
		assertNotNull(dataFile);
		assertNotEquals("${jeptest.datafile}", dataFile);
	}

	@Test
	public final void testGetData() {

		Data data = dBuilder.getData(PyString.class);
		assertNotNull(data);
		data.setData(dataFile);
		// PyString encapsulates contained data in quotes on returning by
		// getData
		assertEquals("\"" + dataFile + "\"", data.getData());
	}

	@Test
	public final void testUsingPythonInRuntime() throws IOException {
		assertNotNull(scriptsFolder);
		String fileName = scriptsFolder + "/test1.py";
		init(fileName);

		int number1 = 10;
		int number2 = 32;
		Process p = Runtime.getRuntime().exec("python " + fileName + " " + number1 + " " + number2);
		int ret = getResult(p.getInputStream());
		System.out.println("value is : " + ret);
		assertEquals(42, ret);
	}

	@Test
	public final void testUsingPythonInProcessBuilder() throws IOException {
		assertNotNull(scriptsFolder);
		String fileName = scriptsFolder + "/test1.py";
		init(fileName);

		int number1 = 10;
		int number2 = 32;
		ProcessBuilder pb = new ProcessBuilder("python", fileName, "" + number1, "" + number2);
		Process p = pb.start();
		int ret = getResult(p.getInputStream());
		System.out.println("value is : " + ret);
		assertEquals(42, ret);
	}

	@Test
	public final void testJep() throws JepException {
		System.out.println(System.getProperty("java.library.path"));
		assertNotNull(scriptsFolder);

		jep.eval("import sys");
		jep.eval("print sys.path");
		System.out.println(jep.getValue("sys.path"));

		// jep.eval("import os");
		// execfile("script2.py 1")

		jep.runScript(scriptsFolder + "/init.py");
		jep.close();

	}

	void init(String file) throws IOException {
		String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(prg);
		out.close();
	}

	int getResult(InputStream stream) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		int ret = new Integer(in.readLine()).intValue();
		in.close();
		return ret;
	}

	@Test
	public final void testGettingFunctionsDescriptions() {
		assertNotNull(framework);
		Set<String> names = framework.getFunctionDescriptors();
		assertThat(names, not(empty()));
		System.out.println(Arrays.toString(names.toArray()));

		assertThat(names, containsInAnyOrder("Python_ReadFile", "Python_ToMainProgram", "Python_ScaleDataset"));
	}

	@Test
	public final void testGetFunction() throws FileNotFoundException, IOException {
		Set<String> names = framework.getFunctionDescriptors();
		assertThat(names, not(empty()));

		Properties properties = new Properties();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("jep.properties").getFile());

		try (InputStream input = new FileInputStream(file)) {
			properties.load(input);
			assertFalse(properties.isEmpty());

			System.out.println(Arrays.toString(properties.values().toArray()));

			for (String nm : names) {
				DMCFunction func = framework.getDMCFunction(nm);
				assertEquals(nm, func.getName());
				assertThat(properties.values(), hasItem(func.getDescription()));
			}
		}

	}

}
