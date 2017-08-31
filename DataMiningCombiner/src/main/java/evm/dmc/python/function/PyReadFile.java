package evm.dmc.python.function;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.Function;
import evm.dmc.python.PythonFramework;
import evm.dmc.python.data.PandasDataFrame;

@Service("Python_ReadFile")
@PropertySource("classpath:jep.properties")
@Function
public class PyReadFile extends AbstractPythonFunction {
	private final static String name = "Python_ReadFile";
	private static final FunctionType TYPE = FunctionType.CSV_DATASOURCE;
	private Properties functionProperties = new Properties();
	static final Integer argCount = 1;
	@Autowired
	PythonFramework fw;

	@Value("${jep.readFileCSV ?: fileops.readCSV}")
	String readCSV;

	@Value("${jep.readFileCSV_desc}")
	String description;

	// @Value("#{pythonFramework.getData(Python_DataFrame.getClass())}")
	@Autowired
	private PandasDataFrame result;

	public PyReadFile() {
		super();
	}

	@PostConstruct
	public void init() {
		super.setFunction(readCSV);
		super.setResult(result);
		// super.setResult( fw.getData(PandasDataFrame.class));
	}

	@Override
	public Integer getArgsCount() {
		return argCount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	protected FunctionType getFunctionType() {
		return TYPE;
	}

	@Override
	protected Properties getProperties() {
		return functionProperties;
	}

	@Override
	protected void setFunctionProperties(Properties funProperties) {
		// TODO Auto-generated method stub
		
	}

}
