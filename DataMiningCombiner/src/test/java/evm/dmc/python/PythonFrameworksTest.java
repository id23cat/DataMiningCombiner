package evm.dmc.python;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import jep.Jep;
import jep.JepException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCPythonConfig.class)
// @SpringBootTest
@PropertySource("classpath:jep.properties")
public class PythonFrameworksTest {
	@Autowired
	private Jep jep;

	@Value("${jep.scriptsFolder}")
	private String scriptsFolder;

	@Value("${jep.execFile}")
	private String execute;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

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

	// Too slow!!!
	// @Test
	// public final void testJython() {
	// PythonInterpreter python = new PythonInterpreter();
	//
	// int number1 = 10;
	// int number2 = 32;
	//
	// python.set("number1", new PyInteger(number1));
	// python.set("number2", new PyInteger(number2));
	// python.exec("number3 = number1+number2");
	// PyObject number3 = python.get("number3");
	// System.out.println("val : " + number3.toString());
	// }

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

	// private void runAsCommand() throws IOException {
	// // String prg = "import sys; print int(sys.argv[1])+int(sys.argv[2])";
	// String prg = "print int(10)+int(20)";
	// int number1 = 10;
	// int number2 = 32;
	// // Process p = Runtime.getRuntime().exec("python -c \"" + prg + "\" " +
	// // number1 + " " + number2);
	// Process p = Runtime.getRuntime().exec("python -c \"" + prg + "\"");
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	// int ret = new Integer(in.readLine()).intValue();
	// System.out.println("value is : " + ret);
	// in.close();
	// }
}
