package evm.dmc.core.arithmetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.BiFunction;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;
//import evm.dmc.core.arithmetic.DivFunction;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.DMCFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ArithmeticPackageConfig.class)
public class DivFunctionTest {

	@Autowired
	private DivFunction divF;

	Data<Integer> x15 = new IntegerData(15);
	Data<Integer> y5 = new IntegerData(5);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testAutowiredObject() {
		assertNotNull(divF);
	}

	/**
	 * Testing of division function implemented in DivFunction class
	 */
	@Test
	public final void testDivIntegerDataIntegerData() {
		Data<Integer> r3 = new IntegerData(3);
		BiFunction<Data<Integer>, Data<Integer>, Data<Integer>> func;
		// a / b
		func = divF::div;

		// 15 / 5 = 3
		Data<Integer> result = func.apply(x15, y5);
		assertEquals(result, r3);
	}

	@Test
	public final void testBaseMethodExecute() {
		Data<Integer> r30 = new IntegerData(30);
		ArithmeticContext context = (ArithmeticContext) divF.getContext();
		DMCFunction<Integer> funcInterface = divF;
		context.setMultiplier(10);

		funcInterface.addArgument(x15);
		funcInterface.addArgument(y5);
		// (15 / 5) * 10 = 30
		funcInterface.execute();
		Data<Integer> result = funcInterface.getResult();
		assertEquals(result, r30);
	}

	/**
	 * Testing of function name
	 */
	@Test
	public final void testGetName() {
		String descr = "Div function";
		assertEquals(descr, divF.getName(), descr);
		divF.setName("Division arguments");
		assertEquals(divF.getName(), "Division arguments");
	}

	@Test
	public final void testGetArgsCount() {
		assertEquals(divF.getArgsCount(), Integer.valueOf(2));
	}

}
