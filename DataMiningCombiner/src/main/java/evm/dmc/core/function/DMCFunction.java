package evm.dmc.core.function;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.List;

import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;

@Scope("prototype")
@Lazy
public interface DMCFunction<T> {
	void execute();

	/**
	 * Returns name of function that should match the pattern:
	 * "frameworkName_functionName"
	 * 
	 * @return function name
	 */
	String getName();

	/**
	 * Returns detailed description of the function
	 * 
	 * @return description
	 */
	String getDescription();

	Integer getArgsCount();

	// void setName(String name);

	// void setArgsCount(Integer count);

	// @Deprecated
	// void addArgument(Data<T> arg);

	@SuppressWarnings("unchecked")
	void setArgs(Data<T>... datas);

	void setArgs(List<Data<T>> largs);

	Data<T> getResult();

	/**
	 * Returns object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	default FrameworkContext getContext() {
		return null;
	}

	/**
	 * Sets object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	default void setContext(FrameworkContext context) {

	}

}
