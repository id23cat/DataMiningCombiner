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
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.DMCFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ArithmeticPackageConfig.class)
public class MulFunctionTest {

	@Autowired
	MulFunction mulF;

	IntegerData x4 = new IntegerData(4);
	IntegerData y5 = new IntegerData(5);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testAutowiredObject() {
		assertNotNull(mulF);
	}

	@Test
	public final void testMulIntegerDataIntegerData() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;

		// a * b
		func = mulF::mul;

		IntegerData r20 = new IntegerData(20);
		// 4 * 5 = 20
		assertEquals(func.apply(x4, y5), r20);
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testBaseMethodExecute() {
		ArithmeticContext context = (ArithmeticContext) mulF.getContext();
		DMCFunction<Integer> funcInterface = mulF;
		context.setMultiplier(10);

		// funcInterface.addArgument(x4);
		// funcInterface.addArgument(y5);

		funcInterface.setArgs(x4, y5);
		Data<Integer> r200 = new IntegerData(200);
		// 4 * 5 * 10 = 200
		funcInterface.execute();
		Data<Integer> result = funcInterface.getResult();
		assertEquals(result, r200);
	}

	@Test
	public final void testGetName() {
		String descr = "Mul function";
		assertEquals(descr, mulF.getName(), descr);
		mulF.setName("Multiplication arguments");
		assertEquals(mulF.getName(), "Multiplication arguments");
	}

	@Test
	public final void testGetArgsCount() {
		assertEquals(mulF.getArgsCount(), Integer.valueOf(2));
	}

}
