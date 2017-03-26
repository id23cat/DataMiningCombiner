package evm.dmc.core.arithmetic;

import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.AbstractDMCFunction;

/**
 * @author id23cat
 * Offers biargument Division function
 */
public class DivFunction extends AbstractDMCFunction {

	IntegerData context = new IntegerData(50);

	public DivFunction() {
		super();
		super.setName("Div function");
		super.setArgsCount(2);
	}
	
	public DivFunction(IntegerData context) {
		this();
		this.context = context;
	}

	/**
	 * Execute division: {@code a / b + context}
	 * @param a
	 * @param b
	 * @return 
	 */
	public IntegerData div(IntegerData a, IntegerData b) {
		return new IntegerData((a.getData() / b.getData()) + context.getData());
	}
	
	public Integer div(Integer a, Integer b){
		return ((a / b) + context.getData());
	}
}
