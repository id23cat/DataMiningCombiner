package evm.dmc.core.arithmetic;

import org.springframework.stereotype.Component;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Division function
 */
@Component
public class DivFunction extends AbstractArithmeticFunction<Integer> {

	public DivFunction() {
		super();
		super.setName("Div function");
		super.setArgsCount(2);
		
		// The most important setting
		super.setFunction(this::div);
	}

	/**
	 * Execute division: {@code a / b + context}
	 * @param a
	 * @param b
	 * @return 
	 */
	public Integer div(Integer a, Integer b){
		return a / b;
	}
	
	public IntegerData div(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( div(a.getData(), b.getData()) );
	}
	
}
