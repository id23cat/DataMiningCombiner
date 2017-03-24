package evm.dmc.core.arithmetic;

import evm.dmc.core.AbstactFunction;
import evm.dmc.core.Data;

public class AddFunction extends AbstactFunction {

//	/* 
//	 * Allows to set only 2 parameters
//	 * @param paramCount doesn't influence anything
//	 */
//	@Override
//	public
//	final void setParamCount(Integer paramCount) {
//		super.paramCount = 2; 	
//	}
	
	

	public AddFunction() {
		super();
		super.paramCount = 2;
	}

	@Override
	public void execute() {
		result.setData(dataArguments.get(0).getData() + dataArguments.get(1).getData());

	}

}
