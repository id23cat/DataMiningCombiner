package evm.dmc.python.data;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("Python_DataFrame")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PandasDataFrame extends JepVariable {

	public PandasDataFrame() {
		super();
	}

	public PandasDataFrame(String variable) {
		super(variable);
	}

}
