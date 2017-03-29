/**
 * 
 */
package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.DMCFunction;

/**
 * @author id23cat
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ArithmeticPackageConfig.class)
public class AddFunctionTest {

	@Autowired
	AddFunction addF;

	IntegerData x3 = new IntegerData(3);
	IntegerData y5 = new IntegerData(5);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testAutowiredObject() {
		assertNotNull(addF);
	}

	/**
	 * Test method for
	 * {@link evm.dmc.core.arithmetic.AddFunction#add(evm.dmc.core.data.IntegerData, evm.dmc.core.data.IntegerData)}.
	 */
	@Test
	public final void testAdd() {
		IntegerData r8 = new IntegerData(8);
		BiFunction<IntegerData, IntegerData, IntegerData> func;

		// a + b
		func = addF::add;

		// 3+5 = 8
		assertEquals(func.apply(x3, y5), r8);
	}
	
	@Test
	public final void testBaseMethodExecute() {
		ArithmeticContext context = (ArithmeticContext) addF.getContext();
		context.setMultiplier(10);
		
		DMCFunction<Integer> funcInterface = addF;
		
		funcInterface.addArgument(x3);
		funcInterface.addArgument(y5);
		IntegerData r80 = new IntegerData(80);
		// (3+5) * 10 = 80
		funcInterface.execute();
		Data<Integer> result = funcInterface.getResult();
		assertEquals(result, r80);
	}

	/**
	 * Test method for
	 * {@link evm.dmc.core.function.AbstractDMCFunction#getName()}.
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
