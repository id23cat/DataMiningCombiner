package evm.dmc.core.arithmetic;

import org.springframework.stereotype.Component;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 * Offers biargument Addition function
 */
@Component
public class AddFunction extends AbstractArithmeticFunction<Integer>{
	
	public AddFunction() {		
		super();
		super.setName("Add function");
		super.setArgsCount(2);
		
		// The most important setting
		super.setFunction(this::add);
	}
	
	/**
	 * Execute addition: {@code (a + b) * context}
	 * @param a
	 * @param b
	 * @return 
	 */
	public Integer add(Integer a, Integer b){
		return a + b;
	}
	
	public IntegerData add(Data<Integer> a, Data<Integer> b) {
		return new IntegerData( add((Integer)a.getData(), (Integer)b.getData()) );
	}

}
