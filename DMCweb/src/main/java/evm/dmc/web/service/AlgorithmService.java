package evm.dmc.web.service;

import java.util.Optional;
import java.util.Set;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;

public interface AlgorithmService extends EnityGetter<Algorithm>, EntityModifier<Algorithm>{
	
	
	Algorithm addNew(ProjectModel project, Algorithm algorithm);
	
	
	static Algorithm getNewAlgorithm() {
		return new Algorithm();
	}
//	static Algorithm getNewFunction(FunctionModel function) {
//		FWFunction alg =  new FWFunction();
//		alg.setFunctionModel(function);
//		return alg;
//	}
}
