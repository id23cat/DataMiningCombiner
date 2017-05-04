package evm.dmc.weka.function;

import org.springframework.stereotype.Service;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.AbstractDMCFunction;
import weka.core.Instances;

@Service
public abstract class AbstractWekaFunction extends AbstractDMCFunction<Instances> {

	protected Data<Instances> result = null;

	@Override
	public Data<Instances> getResult() {
		return result;
	}

}
