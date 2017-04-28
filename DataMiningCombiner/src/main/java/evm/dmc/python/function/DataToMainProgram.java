package evm.dmc.python.function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.core.data.StringData;
import evm.dmc.python.AbstractPythonFunction;

@Service("Python_ToMainProgram")
@PropertySource("classpath:jep.properties")
public class DataToMainProgram extends AbstractPythonFunction {
	String function = "getData"; // content doesn't matter, it needs only to be
									// correctly checked
	// @Autowired
	// PythonFramework fw;

	static final Integer argCount = 1;

	@Autowired
	private StringData result;

	public DataToMainProgram() {
		super();
		super.setName("Load data from Python to java environment");
		super.setArgsCount(argCount);

	}

	@PostConstruct
	public void init() {
		super.setResult(result);
		// super.setResult(fw.getData(PandasDataFrame.class));
	}

	@Override
	public void execute() {
		super.check();
		result = super.convert(super.arguments.get(0));
	}

	@Override
	public Integer getArgsCount() {
		return argCount;
	}
}
