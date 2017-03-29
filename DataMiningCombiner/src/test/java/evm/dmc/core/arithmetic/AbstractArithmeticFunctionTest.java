package evm.dmc.core.arithmetic;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ArithmeticPackageConfig.class)
public class AbstractArithmeticFunctionTest {
	
	@Autowired
	private ArithmeticContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testArithmeticContextCreation() {
		assertNotNull(context);
	}

}
