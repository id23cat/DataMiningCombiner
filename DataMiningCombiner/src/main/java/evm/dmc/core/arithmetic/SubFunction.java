package evm.dmc.core.arithmetic;

import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.AbstractDMCFunction;

/**
 * @author id23cat
 * Offers biargument Substitution function
 */
public class SubFunction extends AbstractDMCFunction {

	IntegerData context = new IntegerData(100);

	public SubFunction() {
		super();
		super.setName("Sub function");
		super.setArgsCount(2);
	}
	
	public SubFunction(IntegerData context) {
		this();
		this.context = context;
	}
	
	/**
	 * Execute substitution: {@code (a - b) * context}
	 * @param a
	 * @param b
	 * @return
	 */
	public IntegerData sub(IntegerData a, IntegerData b) {
		return new IntegerData((a.getData() - b.getData()) * context.getData());
	}
	
	public Integer sub(Integer a, Integer b){
		return ((a - b) * context.getData());
	}

}
