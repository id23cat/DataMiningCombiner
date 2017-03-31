package evm.dmc.core.arithmetic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat Offers biargument Substitution function
 */
@Service("Arithmetic_Sub")
public class SubFunction extends AbstractArithmeticFunction<Integer> {
	@Autowired
	@ArithmeticFW
	Framework fw;

	public SubFunction() {
		super();
		super.setName("Sub function");
		super.setArgsCount(2);

		// The most important setting
		super.setFunction(this::sub);
	}

	/**
	 * Execute substitution: {@code (a - b) * context}
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer sub(Integer a, Integer b) {
		return a - b;
	}

	public IntegerData sub(Data<Integer> a, Data<Integer> b) {
		return (IntegerData) fw.getData(sub(a.getData(), b.getData()));
	}

}
