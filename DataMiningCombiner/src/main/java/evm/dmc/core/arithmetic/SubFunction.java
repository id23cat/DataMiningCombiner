package evm.dmc.core.arithmetic;

import org.springframework.stereotype.Component;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Substitution function
 */
@Component
public class SubFunction extends AbstractArithmeticFunction<Integer> {

	public SubFunction() {
		super();
		super.setName("Sub function");
		super.setArgsCount(2);
		
		// The most important setting
		super.setFunction(this::sub);
	}
	
	/**
	 * Execute substitution: {@code (a - b) * context}
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer sub(Integer a, Integer b){
		return a - b;
	}
	
	public IntegerData sub(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( sub(a.getData(), b.getData()) );
	}

}
