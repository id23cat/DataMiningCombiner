package evm.dmc.core.services;

import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContextAware;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

public interface FrameworksService extends ApplicationContextAware{
	Set<String> getFrameworksDescriptors();
	
	Framework getFramework(String descriptor);
	
	Map<String, String> getFunctionsDescriptors();
	
	DMCFunction<?> getFunction(String descriptor) throws NoSuchFunctionException;
	
	DMCFunction<?> getFunction(FunctionModel model) throws NoSuchFunctionException;
	
	DMCDataLoader getDataLoaderFunction(String descriptor) throws NoSuchFunctionException;
	
	DMCDataSaver getDataSaverFunction(String descriptor) throws NoSuchFunctionException;
	
	DMCFunction<?> getFunction(String descriptor, String framework) throws NoSuchFunctionException;
	
	DMCFunction<?> getFunction(String descriptor, Framework framework) throws NoSuchFunctionException;


}
