package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;

import evm.dmc.core.data.IntegerData;

public class SubFunctionTest {

	SubFunction subF = new SubFunction(new IntegerData(10));
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testSubIntegerDataIntegerData() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;
		
		// (a - b) * context
		func = subF::sub;
		
		IntegerData x5 = new IntegerData(5);
		IntegerData y2 = new IntegerData(2);
		IntegerData r30 = new IntegerData(30);
		// (5 - 2) * 10 = 30
		assertEquals(func.apply(x5, y2), r30);
	}

	@Test
	public final void testGetName() {
		String descr = "Sub function";
		assertEquals(descr, subF.getName(), descr);
		subF.setName("Substitution arguments");
		assertEquals(subF.getName(), "Substitution arguments");
	}

	@Test
	public final void testGetArgsCount() {
		assertEquals(subF.getArgsCount(), Integer.valueOf(2));
	}

}
