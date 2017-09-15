package evm.dmc.core.api;

import java.util.Set;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

/**
 * The main class which manages all bottom processing of algorithm, functions and data
 * 
 * @author id23cat
 *
 */
public interface Project {

//	static Project CreateProject() {
//		return new SimplestProjectImpl();
//	}

	Project setModel(ProjectModel model);

	ProjectModel getModel();

	Algorithm createAlgorithm();

	Algorithm createAlgorithm(AlgorithmModel algModel);

	Algorithm getAlgorithm();
	
	Project setProjectName(String name);
	
	String getPrijectName();
	
	FrameworksRepository getFunctionsRepository();
	
	Set<String> getFunctionsSet();
	
	Set<String> getDataLoadersSet();
	
	Set<String> getDataSaversSet();
	
	FunctionModel getFunctionModel(String descriptor) throws NoSuchFunctionException;
	
	FunctionSrcModel getDataSrcModel(String decriptor) throws NoSuchFunctionException;
	
	FunctionDstModel getDataDstModel(String descriptor) throws NoSuchFunctionException;
	
	Project setDataSrc(Algorithm algorithm, FunctionSrcModel srcModel);
	
	Project setDataDst(Algorithm algorithm, FunctionDstModel dstModel);
	
	Project addFunction(Algorithm algorithm, FunctionModel funModel) throws NoSuchFunctionException;
	
	Project insertFunction(Algorithm algorithm, FunctionModel funModel, Integer position) throws NoSuchFunctionException;
	
	Project insertFunctionAfter(Algorithm algorithm, FunctionModel funModel, FunctionModel after) throws NoSuchFunctionException;
	
	Project insertFunctionBefore(Algorithm algorithm, FunctionModel funModel, FunctionModel before) throws NoSuchFunctionException;
	

}
