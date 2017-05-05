package evm.dmc.core.arithmetic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.core.DataFactory;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat Offers biargument Multiplication function
 */
@PropertySource("classpath:arithmetic.properties")
@Service("Arithmetic_Mul")
public class MulFunction extends AbstractArithmeticFunction<Integer> {
	@Autowired
	@ArithmeticFW
	Framework fw;

	@Autowired
	@ArithmeticFW
	DataFactory dBuilder;

	static final Integer argCount = 2;

	@Value("${arith.mul_name}")
	String name;

	@Value("${arith.mul_desc}")
	String description;

	public MulFunction() {
		super();
		// The most important setting
		super.setFunction(this::mul);
	}

	/**
	 * Execute multiplication: {@code (a * b) / context}
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer mul(Integer a, Integer b) {
		return a * b;
	}

	public IntegerData mul(Data<Integer> a, Data<Integer> b) {
		return (IntegerData) dBuilder.getData(mul(a.getData(), b.getData()));
	}

	@Override
	public Integer getArgsCount() {
		return argCount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {

		return description;
	}

}
