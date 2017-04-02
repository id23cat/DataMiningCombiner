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

	default String getName() {
		return "Warning: No name specified";
	}

	Integer getArgsCount();

	void setName(String name);

	// void setArgsCount(Integer count);

	// @Deprecated
	// void addArgument(Data<T> arg);

	@SuppressWarnings("unchecked")
	void setArgs(Data<T>... datas);

	void setArgs(List<Data<T>> largs);

	Data<T> getResult();

	// Data<T> getResult;

	/**
	 * Returns object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	FrameworkContext getContext();

	/**
	 * Sets object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	void setContext(FrameworkContext context);

	/**
	 * @author id23cat Interface describes requirements to the context of
	 *         execution for concrete function
	 */

}
