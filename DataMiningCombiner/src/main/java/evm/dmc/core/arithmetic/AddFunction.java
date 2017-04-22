package evm.dmc.core.arithmetic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.DataBuilder;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat Offers biargument Addition function
 */
@Service("Arithmetic_Add")
public class AddFunction extends AbstractArithmeticFunction<Integer> {
	@Autowired
	@ArithmeticFW
	Framework fw;

	@Autowired
	@ArithmeticFW
	DataBuilder dBuilder;

	public AddFunction() {
		super();
		super.setName("Add function");
		super.setArgsCount(2);

		// The most important setting
		super.setFunction(this::add);
	}

	/**
	 * Execute addition: {@code (a + b) * context}
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer add(Integer a, Integer b) {
		return a + b;
	}

	public IntegerData add(Data<Integer> a, Data<Integer> b) {
		return (IntegerData) dBuilder.getData(add(a.getData(), b.getData()));
	}

}
