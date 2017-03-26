/**
 * 
 */
package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;

import evm.dmc.core.data.IntegerData;

/**
 * @author id23cat
 *
 */
public class AddFunctionTest {

	AddFunction addF = new AddFunction(new IntegerData(10));
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test method for {@link evm.dmc.core.arithmetic.AddFunction#add(evm.dmc.core.data.IntegerData, evm.dmc.core.data.IntegerData)}.
	 */
	@Test
	public final void testAdd() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;
		
		// (a + b) * context
		func = addF::add;
		
		IntegerData x3 = new IntegerData(3);
		IntegerData y5 = new IntegerData(5);
		IntegerData r80 = new IntegerData(80);
		// (3+5)*10 = 80
		assertEquals(func.apply(x3,y5), r80);
		
		
	}

	/**
	 * Test method for {@link evm.dmc.core.function.AbstractDMCFunction#getName()}.
	 */
	@Test
	public final void testGetName() {
		String descr = "Add function";
		assertEquals(descr, addF.getName(), descr);
		addF.setName("Addition arguments");
		assertEquals(addF.getName(), "Addition arguments");
	}
	
	@Test
	public final void testGetArgsCount() {
		assertEquals(addF.getArgsCount(), Integer.valueOf(2));
	}

}
