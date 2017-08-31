package evm.dmc.python.function;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.Function;

@Service("Python_ScaleDataset")
@PropertySource("classpath:jep.properties")
@Function
public class ScaleDataset extends AbstractPythonFunction {
	private final static String name = "Python_ScaleDataset";
	private final static Integer ARGS_COUNT = 1; // TODO must also accept
													// attributes like list of
													// attributes which need to
													// have in rest
	private static final FunctionType TYPE = FunctionType.OTHER;
	private Properties functionProperties = new Properties();
	
	@Value("${jep.dm_get_scaled}")
	String scale;

	@Value("${jep.dm_get_scaled_desc}")
	String description;

	public ScaleDataset() {
		super();
	}

	@PostConstruct
	public void init() {
		super.setFunction(scale);
		// Data result = super.fw.getData(PyVar.class);
		// super.setResult(result);
		// TODO
	}

	@Override
	public Integer getArgsCount() {

		return ARGS_COUNT;
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
