package evm.dmc.core.arithmetic;

import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.AbstractDMCFunction;

/**
 * @author id23cat
 * Offers biargument Multiplication function
 */
public class MulFunction extends AbstractDMCFunction {

	IntegerData context = new IntegerData(2);
	
	public MulFunction() {
		super();
		super.setName("Mul function");
		super.setArgsCount(2);
	}
	
	public MulFunction(IntegerData context) {
		this();
		this.context = context;
	}

	/**
	 * Execute multiplication: {@code (a * b) / context}
	 * @param a
	 * @param b
	 * @return 
	 */
	public IntegerData mul(IntegerData a, IntegerData b){
		return new IntegerData((a.getData() * b.getData()) / context.getData());
	}
	
	public Integer mul(Integer a, Integer b){
		return ((a * b) / context.getData());
	}

}
