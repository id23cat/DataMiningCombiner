package evm.dmc.core.arithmetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Set;

import evm.dmc.core.Framework;
import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ArithmeticPackageConfig.class)
@SpringBootTest
public class ArithmeticFrameworkTest implements ApplicationContextAware {
	@Autowired
	Framework framework;

	ApplicationContext appContext;

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testAutowiredObject() {
		assertNotNull(framework);
		assertNotNull(appContext);
		System.out.println("Total beans: " + appContext.getBeanDefinitionCount());
		System.out.println(Arrays.toString(appContext.getBeanDefinitionNames()));
		System.out.println(Arrays.toString(appContext.getBeanNamesForType(Framework.class)));
		System.out.println(Arrays.toString(appContext.getBeanNamesForType(DMCFunction.class)));
		System.out.println(Arrays.toString(appContext.getBeanNamesForType(FrameworkContext.class)));
		assertFalse(systemOutRule.getLog().isEmpty());

	}

	@Test
	public final void testInitFrameworkAndGetFunctionDescriptors() {
		framework.initFramework();
		Set<String> descriptors = framework.getFunctionDescriptors();
		assertNotNull(descriptors);
		System.out.println(descriptors);
		assertFalse(systemOutRule.getLog().isEmpty());
		assertTrue(descriptors.contains("Arithmetic_Add"));
		assertTrue(descriptors.contains("Arithmetic_Div"));
		assertTrue(descriptors.contains("Arithmetic_Mul"));
		assertTrue(descriptors.contains("Arithmetic_Sub"));
	}

	@Test
	public final void testGetData() {
		framework.initFramework();
		@SuppressWarnings("unchecked")
		Data<Integer> data1 = framework.getData(12);
		@SuppressWarnings("unchecked")
		Data<Integer> data2 = framework.getData(4);
		assertNotNull(data1);
		assertNotNull(data2);
		assertNotEquals(data1, data2);
		assertEquals(Integer.valueOf(12), data1.getData());
		assertEquals(Integer.valueOf(4), data2.getData());
	}

	@Test
	@SuppressWarnings("unchecked")
	public final void testGetDMCFunctionAndContext() {
		framework.initFramework();

		DMCFunction<Integer> addF = framework.getDMCFunction("Arithmetic_Add");
		testFunction(addF, framework.getData(12), framework.getData(10), 22);

		DMCFunction<Integer> subF = framework.getDMCFunction("Arithmetic_Sub");
		assertNotEquals(addF, subF);
		testFunction(subF, framework.getData(22), framework.getData(10), 12);

	}

	private void testFunction(DMCFunction<Integer> func, Data arg1, Data arg2, Integer expRes) {
		func.setArgs(arg1, arg2);
		func.execute();
		Data<Integer> result = func.getResult();
		assertNotNull(expRes);
		assertEquals(expRes, result.getData());

	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.appContext = appContext;

	}

}
