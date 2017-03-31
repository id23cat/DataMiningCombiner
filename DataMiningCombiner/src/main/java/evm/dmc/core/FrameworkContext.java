package evm.dmc.core;

import org.springframework.context.annotation.Scope;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;

@Scope("singleton")
public interface FrameworkContext {
	/**
	 * Method is used for first initialization of function context or resetting
	 * settings to default
	 */
	void initContext();

	/**
	 * Execute concrete function in context
	 * 
	 * @param function
	 */
	void executeInContext(DMCFunction function);

	Data getNewData();
}
