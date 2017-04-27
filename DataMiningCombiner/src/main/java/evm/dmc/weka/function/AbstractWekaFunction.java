package evm.dmc.weka.function;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.AbstractDMCFunction;
import weka.core.Instances;

public abstract class AbstractWekaFunction extends AbstractDMCFunction<Instances> {

	protected Data<Instances> result;

	@Override
	public Data<Instances> getResult() {
		return result;
	}

}
