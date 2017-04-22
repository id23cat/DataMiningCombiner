package evm.dmc.core;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContextAware;

import java.util.Set;

import evm.dmc.core.function.DMCFunction;

/**
 * The Interface Framework. Interface describes common methods for using
 * Framework objects
 *
 * @author id23cat
 */
public interface Framework extends ApplicationContextAware {

	/**
	 * Method is used for first initialization of framework or resetting
	 * settings to default.
	 */
	@PostConstruct
	void initFramework();

	/**
	 * Method must return list of short descriptors or identifiers of functions
	 * provided by framework.
	 *
	 * @return the function descriptors
	 */
	Set<String> getFunctionDescriptors();

	/**
	 * Gets the DMC function.
	 *
	 * @param descriptor
	 *            the descriptor
	 * @return the DMC function
	 */
	DMCFunction getDMCFunction(String descriptor);
}
