package evm.dmc.core.arithmetic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import evm.dmc.core.Framework;
import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.IntegerData;
import evm.dmc.core.function.DMCFunction;

/**
 * TODO Hard wired to Spring Have to be changed with consideration to:
 * 
 * @see https://spring.io/blog/2004/08/06/method-injection/
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-java-method-injection
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-factory-method-injection
 */
@Service
@ArithmeticFW
public class ArithmeticFramework implements Framework {
	private Set<String> funcDescriptors;
	@Autowired
	@ArithmeticFWContext
	private FrameworkContext frameworkContext;

	private ApplicationContext applicationContext;

	@Override
	public void initFramework() {
		// TODO
		funcDescriptors = new HashSet<String>(
				Arrays.asList(applicationContext.getBeanNamesForType(AbstractArithmeticFunction.class)));

	}

	/**
	 * @return the frameworkContext
	 */
	public FrameworkContext getFrameworkContext() {
		return frameworkContext;
	}

	@Override
	public Set<String> getFunctionDescriptors() {
		return funcDescriptors;
	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		// TODO
		DMCFunction function = applicationContext.getBean(descriptor, AbstractArithmeticFunction.class);
		function.setContext(frameworkContext);
		return function;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO
		this.applicationContext = applicationContext;
	}

	@Override
	public Data getData(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data getData(Object rawData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data getData(Number num) {
		IntegerData data = (IntegerData) instantiateData();
		data.setData((Integer) num);
		return data;
	}

	@Override
	public Data getData(Data otherFormat) {
		IntegerData data = (IntegerData) instantiateData();
		Class<? extends Number> cls = data.getData().getClass();
		data.setData((Integer) cls.cast(otherFormat.getData()));
		return null;
	}

	private Data instantiateData() {
		return applicationContext.getBean(IntegerData.class);
	}

}
