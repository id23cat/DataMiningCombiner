package evm.dmc.core.api.back;

import org.springframework.context.annotation.Scope;

import evm.dmc.core.api.DMCFunction;

/**
 * Represents execution context of some framework
 */
@Scope("singleton")
public interface FrameworkContext {
	/**
	 * Method is used for first initialization of function context or resetting
	 * settings to default.
	 */
	void initContext();

	/**
	 * Execute concrete function in context.
	 * 
	 * @param function      function to execute
	 */
	void executeInContext(DMCFunction function);

	// Data getNewData();

	/**
	 * Pulls out value from possible environment or just copies from arg data to
	 * result of function object.
	 *
	 * @return      result of function execution
	 */
	void getValue(DMCFunction function);
}
