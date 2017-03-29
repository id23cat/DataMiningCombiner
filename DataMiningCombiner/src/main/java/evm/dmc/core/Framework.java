package evm.dmc.core;

import java.util.List;

import evm.dmc.core.function.DMCFunction;

/**
 * @author id23cat Interface describes common methods for using Framework
 *         objects
 */
public interface Framework {

	/**
	 * Method is used for first initialization of framework or resetting settings to default
	 */
	void initFramework();

	/**
	 * Method must return list of short descriptors or identifiers of functions
	 * provided by framework
	 * 
	 * @return
	 */
	List<String> getFunctionDescriptors();


}
