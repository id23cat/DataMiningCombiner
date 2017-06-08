package evm.dmc.python.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.core.data.Data;

/**
 * Methos in this auxiliary class must be tested in PyReadFileTest
 * 
 * @author id23cat
 *
 */
@Service
@PropertySource("classpath:jeptest.properties")
public class DataLoader {

	@Value("#{pythonFramework.getDMCFunction(\"Python_ReadFile\")}")
	// private DMCFunction function;
	private PyReadFile function;

	@Value("${jeptest.datafile}")
	private String dataFile;

	@Value("#{pythonFramework.getData(Python_String.getClass())}")
	private Data pyFileName;

	Data getData() {
		pyFileName.setData(dataFile);
		function.setArgs(pyFileName);

		function.execute();
		Data result = function.getResult();
		return result;
	}

}
