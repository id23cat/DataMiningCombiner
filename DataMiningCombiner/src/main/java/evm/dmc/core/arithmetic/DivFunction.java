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
 * @author id23cat Offers biargument Division function
 */
@PropertySource("classpath:arithmetic.properties")
@Service("Arithmetic_Div")
public class DivFunction extends AbstractArithmeticFunction<Integer> {
	@Autowired
	@ArithmeticFW
	Framework fw;

	@Autowired
	@ArithmeticFW
	DataFactory dBuilder;

	static final Integer argCount = 2;

	@Value("${arith.div_name}")
	String name;

	@Value("${arith.div_desc}")
	String description;

	public DivFunction() {
		super();
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
		return (IntegerData) dBuilder.getData(div(a.getData(), b.getData()));
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
