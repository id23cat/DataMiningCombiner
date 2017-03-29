package evm.dmc.core;

import evm.dmc.core.function.DMCFunction;

public interface FrameworkContext {
	/**
	 * Method is used for first initialization of function context or resetting settings to default
	 */
	void initContext();		
	
	/**
	 * Execute concrete function in context
	 * @param function
	 */
	void  executeInContext(DMCFunction function);
}
