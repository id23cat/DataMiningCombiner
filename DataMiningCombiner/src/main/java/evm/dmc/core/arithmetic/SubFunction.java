package evm.dmc.core.arithmetic;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Substitution function
 */
public class SubFunction extends AbstractArithmeticFunction<Integer> {

	IntegerData context = new IntegerData(100);

	public SubFunction() {
		super();
		super.setName("Sub function");
		super.setArgsCount(2);
		super.setFunction(this::sub);
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
	public Integer sub(Integer a, Integer b){
		return ((a - b) * context.getData());
	}
	
	public IntegerData sub(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( sub(a.getData(), b.getData()) );
	}

}
