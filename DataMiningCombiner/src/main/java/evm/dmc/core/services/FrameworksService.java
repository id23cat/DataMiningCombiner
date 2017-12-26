package evm.dmc.core.services;

import org.springframework.context.ApplicationContextAware;

import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

public interface FrameworksService extends ApplicationContextAware{
	public Framework getFramework(String descriptor);
	
	public DMCFunction<?> getFunction(String descriptor) throws NoSuchFunctionException;
	
	public DMCDataLoader getDataLoaderFunction(String descriptor) throws NoSuchFunctionException;
	
	public DMCDataSaver getDataSaverFunction(String descriptor) throws NoSuchFunctionException;
	
	public DMCFunction<?> getFunction(String descriptor, String framework) throws NoSuchFunctionException;
	
	public DMCFunction<?> getFunction(String descriptor, Framework framework) throws NoSuchFunctionException;


}
