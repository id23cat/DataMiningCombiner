package evm.dmc.python.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.python.AbstractPythonFunction;
import evm.dmc.python.JepVariable;
import evm.dmc.python.PandasDataFrame;
import evm.dmc.python.PythonFramework;

@Service("Python_ReadFile")
@PropertySource("classpath:jep.properties")
public class PyReadFile extends AbstractPythonFunction {
	@Autowired
	PythonFramework fw;

	@Value("${jep.readFileCSV}")
	String readCSV;

	public PyReadFile() {
		super();
		super.setName("Python read file");
		super.setArgsCount(1);

		super.setFunction(readCSV);

		super.setResult((JepVariable) fw.getData(PandasDataFrame.class));
	}

}
