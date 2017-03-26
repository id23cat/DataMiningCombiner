package evm.dmc.core.arithmetic;

import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.NumericDMCFunction;

/**
 * @author id23cat
 * Offers biargument Addition function
 */
public class AddFunction extends NumericDMCFunction /*implements DMC2ArgFunction*/<Integer>{
	
	IntegerData context = new IntegerData(4);	
	
	public AddFunction() {		
		super();
		super.setName("Add function");
		super.setArgsCount(2);
	}
	
	public AddFunction(IntegerData context) {
		this();
		this.context = context;
	}

	/**
	 * Execute addition: {@code (a + b) * context}
	 * @param a
	 * @param b
	 * @return 
	 */
	public IntegerData add(IntegerData a, IntegerData b){
		return new IntegerData((a.getData() + b.getData()) * context.getData());
	}
	
	public Integer add(Integer a, Integer b){
		return ((a + b) * context.getData());
	}

}
