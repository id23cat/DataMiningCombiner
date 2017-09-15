package evm.dmc.core.api;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Set;

import evm.dmc.api.model.FrameworkModel;
//import evm.dmc.core.api.DMCDataLoader;
//import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

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

	Map<String, Class<?>> getSaverDescriptors();

	Map<String, Class<?>> getLoaderDescriptors();

	/**
	 * Gets the DMC function.
	 *
	 * @param descriptor
	 *            the descriptor
	 * @return the DMC function
	 * @throws NoSuchFunctionException TODO
	 */
	DMCFunction getDMCFunction(String descriptor) throws NoSuchFunctionException;
	
	<T> T getDMCFunction(String descriptor, Class<T> type) throws NoSuchFunctionException;

	<T> T getDMCDataSaver(String descriptor, Class<T> type) throws NoSuchFunctionException;

	<T> T getDMCDataLoader(String descriptor, Class<T> type) throws NoSuchFunctionException;
	
	void setFrameworkModel(FrameworkModel frameworkModel);
	
	FrameworkModel getFrameworkModel();

}
