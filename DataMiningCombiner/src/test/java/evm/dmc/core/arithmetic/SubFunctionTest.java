package evm.dmc.core.arithmetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.BiFunction;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.DMCFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ArithmeticPackageConfig.class)
@TestPropertySource("classpath:arithmetic.properties")
public class SubFunctionTest {

	@Autowired
	SubFunction subF;

	IntegerData x5 = new IntegerData(5);
	IntegerData y2 = new IntegerData(2);

	@Value("${arith.sub_name}")
	String name;

	@Value("${arith.sub_desc}")
	String desc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testAutowiredObject() {
		assertNotNull(subF);
	}

	@Test
	public final void testSubIntegerDataIntegerData() {
		BiFunction<IntegerData, IntegerData, IntegerData> func;

		// a - b
		func = subF::sub;

		IntegerData r3 = new IntegerData(3);
		// 5 - 2 = 3
		assertEquals(func.apply(x5, y2), r3);
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testBaseMethodExecute() {
		ArithmeticContext context = (ArithmeticContext) subF.getContext();
		DMCFunction<Integer> funcInterface = subF;
		context.setMultiplier(10);

		// funcInterface.addArgument(x5);
		// funcInterface.addArgument(y2);

		funcInterface.setArgs(x5, y2);
		Data<Integer> r30 = new IntegerData(30);
		// (5 - 2) * 10 = 30
		funcInterface.execute();
		Data<Integer> result = funcInterface.getResult();
		assertEquals(result, r30);
	}

	@Test
	public final void testGetName() {
		assertEquals(name, subF.getName());
	}

	@Test
	public final void testGetDescription() {
		assertEquals(desc, subF.getDescription());
	}

	@Test
	public final void testGetArgsCount() {
		assertEquals(subF.getArgsCount(), Integer.valueOf(2));
	}

}
