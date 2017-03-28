package evm.dmc.core.arithmetic;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Division function
 */
public class DivFunction extends AbstractArithmeticFunction<Integer> {

	IntegerData context = new IntegerData(50);

	public DivFunction() {
		super();
		super.setName("Div function");
		super.setArgsCount(2);
		super.setFunction(this::div);
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
	public Integer div(Integer a, Integer b){
		return ((a / b) + context.getData());
	}
	
	public IntegerData div(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( div(a.getData(), b.getData()) );
	}
	
}
