package evm.dmc.core.arithmetic;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Addition function
 */
public class AddFunction extends AbstractArithmeticFunction<Integer>{
	
	IntegerData context = new IntegerData(4);	
	
	public AddFunction() {		
		super();
		super.setName("Add function");
		super.setArgsCount(2);
		super.setFunction(this::add);
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
	public Integer add(Integer a, Integer b){
		return ((a + b) * context.getData());
	}
	
	public IntegerData add(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( add((Integer)a.getData(), (Integer)b.getData()) );
	}

}
