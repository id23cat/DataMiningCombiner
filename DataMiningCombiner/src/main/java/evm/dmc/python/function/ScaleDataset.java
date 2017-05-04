package evm.dmc.python.function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.core.data.Data;
import evm.dmc.python.data.PyVar;

@Service("Python_ScaleDataset")
@PropertySource("classpath:jep.properties")
public class ScaleDataset extends AbstractPythonFunction {

	private final static Integer ARGS_COUNT = 1; // TODO must also accept
													// attributes like list of
													// attributes which need to
													// have in rest

	@Value("${jep.dm_get_scaled}")
	String scale;

	public ScaleDataset() {
		super();
		super.setName("Python scale dataset");
		// super.setArgsCount(ARGS_COUNT);
	}

	@PostConstruct
	public void init() {
		super.setFunction(scale);
		Data result = super.fw.getData(PyVar.class);
		super.setResult(result);
	}

	@Override
	public Integer getArgsCount() {

		return ARGS_COUNT;
	}

}
