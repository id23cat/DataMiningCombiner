package evm.dmc.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;

/**
 * TODO Hard wired to Spring Have to be changed with consideration to:
 * 
 * @see https://spring.io/blog/2004/08/06/method-injection/
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-java-method-injection
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-factory-method-injection
 */
public abstract class AbstractFramework implements Framework {

	private ApplicationContext applicationContext;
	private Set<String> funcDescriptors;

	public AbstractFramework() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO
		this.applicationContext = applicationContext;
		initFramework();
	}

	public void initFrameworkForType(Class type) {
		// TODO
		funcDescriptors = new HashSet<String>(Arrays.asList(applicationContext.getBeanNamesForType(type)));

	}

	@Override
	public Set<String> getFunctionDescriptors() {
		return funcDescriptors;
	}

	public DMCFunction getDMCFunction(String descriptor, Class type) {
		// TODO
		@SuppressWarnings({ "unchecked", "rawtypes" })
		DMCFunction function = (DMCFunction) applicationContext.getBean(descriptor, type);
		return function;
	}

	@Override
	public Data getData(String file) {
		throw new UnsupportedOperationException("Loading from a file not supported");
	}

	@Override
	public Data getData(Object rawData) {
		throw new UnsupportedOperationException("Convertion from unknown type not supported");
	}

	@Override
	public Data getData(Number num) {
		throw new UnsupportedOperationException("Convertion from number type not supported");
	}

	@Override
	public Data getData(Data otherFormat) {
		throw new UnsupportedOperationException("Convertion from other Data type not supported");
	}

	protected Data instantiateData(Class type) {
		return (Data) applicationContext.getBean(type);
	}

}
