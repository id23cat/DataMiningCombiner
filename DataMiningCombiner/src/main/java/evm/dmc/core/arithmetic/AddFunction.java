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
 * @author id23cat Offers biargument Addition function
 */
@PropertySource("classpath:arithmetic.properties")
@Service("Arithmetic_Add")
public class AddFunction extends AbstractArithmeticFunction<Integer> {
	static final Integer argCount = 2;

	@Autowired
	@ArithmeticFW
	Framework fw;

	@Autowired
	@ArithmeticFW
	DataFactory dBuilder;

	@Value("${arith.add_name}")
	String name;

	@Value("${arith.add_desc}")
	String description;

	public AddFunction() {
		super();

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
