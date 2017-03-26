package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;

import evm.dmc.core.data.IntegerData;

public class DivFunctionTest {

	DivFunction divF = new DivFunction(new IntegerData(10));
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testDivIntegerDataIntegerData() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;
		
		// a / b + context
		func = divF::div;
		
		IntegerData x15 = new IntegerData(15);
		IntegerData y5 = new IntegerData(5);
		IntegerData r13 = new IntegerData(13);
		// 15 / 5 + 10 = 13
		assertEquals(func.apply(x15,y5), r13);
	}

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
