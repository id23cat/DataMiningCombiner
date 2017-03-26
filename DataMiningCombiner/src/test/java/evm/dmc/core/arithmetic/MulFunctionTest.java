package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;

import evm.dmc.core.data.IntegerData;

public class MulFunctionTest {

	MulFunction mulF = new MulFunction(new IntegerData(10));

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testMulIntegerDataIntegerData() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;
		
		// a * b / context
		func = mulF::mul;
		
		IntegerData x4 = new IntegerData(4);
		IntegerData y5 = new IntegerData(5);
		IntegerData r2 = new IntegerData(2);
		// 4 * 5 / 10 = 2
		assertEquals(func.apply(x4, y5), r2);
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
