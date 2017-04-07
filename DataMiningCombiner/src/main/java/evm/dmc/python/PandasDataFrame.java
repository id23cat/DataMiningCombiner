package evm.dmc.python;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("Python_DataFrame")
@Scope("protorype")
public class PandasDataFrame extends JepVariable {

	public PandasDataFrame(String variable) {
		super(variable);
	}

}
