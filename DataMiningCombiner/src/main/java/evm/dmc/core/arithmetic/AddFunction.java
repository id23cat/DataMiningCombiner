package evm.dmc.core.arithmetic;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.NumericData;
import evm.dmc.core.function.AbstractDMCFunction;

public class AddFunction extends AbstractDMCFunction{

	public AddFunction() {
		super();
		super.paramCount = 2;
		super.function = (x,y) -> x+y;
	}

	@Override
	public void execute() {

	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Function#getResult()
	 */
	@Override
	public Data getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Function#setSingleArgument(evm.dmc.core.data.Data)
	 */
	@Override
	public void setSingleArgument(NumericData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getArgsCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArgsCount(Integer count) {
		// TODO Auto-generated method stub
		
	}

}
