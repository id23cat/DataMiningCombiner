package evm.dmc.core.arithmetic;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Multiplication function
 */
public class MulFunction extends AbstractArithmeticFunction<Integer> {

	IntegerData context = new IntegerData(2);
	
	public MulFunction() {
		super();
		super.setName("Mul function");
		super.setArgsCount(2);
		super.setFunction(this::mul);
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
	public Integer mul(Integer a, Integer b){
		return ((a * b) / context.getData());
	}
	
	public IntegerData mul(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( mul(a.getData(), b.getData()) );
	}	

}
