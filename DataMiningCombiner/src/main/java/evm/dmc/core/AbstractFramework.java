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
	private Class abstractFunctionClass;

	public AbstractFramework() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO
		this.applicationContext = applicationContext;
		initFramework();
	}

	/**
	 * Initialize the framework for a function type. This type will be used as
	 * main class type for instantiating functions by name in
	 * {@link #getDMCFunction(String)}
	 *
	 * @param functionsClass
	 *            the functions class
	 */
	public void initFrameworkForType(Class functionsClass) {
		// TODO
		funcDescriptors = new HashSet<String>(Arrays.asList(applicationContext.getBeanNamesForType(functionsClass)));

	}

	@Override
	public Set<String> getFunctionDescriptors() {
		return funcDescriptors;
	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		// TODO
		@SuppressWarnings({ "unchecked", "rawtypes" })
		DMCFunction function = (DMCFunction) applicationContext.getBean(descriptor, abstractFunctionClass);
		return function;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Data instantiateData(Class dataType) {
		return (Data) applicationContext.getBean(dataType);
	}

}
