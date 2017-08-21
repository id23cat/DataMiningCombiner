package evm.dmc.core.api.back;

public interface HasNameAndDescription {
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
}
