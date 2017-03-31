package evm.dmc.core.arithmetic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat Offers biargument Division function
 */
@Service("Arithmetic_Div")
public class DivFunction extends AbstractArithmeticFunction<Integer> {
	@Autowired
	@ArithmeticFW
	Framework fw;

	public DivFunction() {
		super();
		super.setName("Div function");
		super.setArgsCount(2);

		// The most important setting
		super.setFunction(this::div);
	}

	/**
	 * Execute division: {@code a / b + context}
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer div(Integer a, Integer b) {
		return a / b;
	}

	public IntegerData div(Data<Integer> a, Data<Integer> b) {
		return (IntegerData) fw.getData(div(a.getData(), b.getData()));
	}

}
