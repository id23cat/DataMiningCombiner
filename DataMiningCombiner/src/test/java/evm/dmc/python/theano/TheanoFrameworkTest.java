package evm.dmc.python.theano;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import jep.Jep;
import jep.JepException;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = ArithmeticPackageConfig.class)
//@SpringBootTest
public class TheanoFrameworkTest {
	String fileName = "Data/test1.py";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testUsingPythonInRuntime() throws IOException {
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
	public final void testJython() {
		PythonInterpreter python = new PythonInterpreter();

		int number1 = 10;
		int number2 = 32;

		python.set("number1", new PyInteger(number1));
		python.set("number2", new PyInteger(number2));
		python.exec("number3 = number1+number2");
		PyObject number3 = python.get("number3");
		System.out.println("val : " + number3.toString());
	}

	@Test
	public final void testJep() throws JepException {
		Jep jep = new Jep(false);
		jep.eval("import numpy as np");
		jep.eval("np.version");
		jep.eval("import pandas as pd");
		jep.eval("pd.__version__");
		// jep.eval("df = pd.read_csv('Data/telecom_churn.csv')");
		// jep.eval("import os");
		// jep.eval("os.getcwd()");
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
