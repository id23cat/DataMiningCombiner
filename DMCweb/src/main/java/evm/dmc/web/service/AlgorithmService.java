package evm.dmc.web.service;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.AlgorithmFunction;
import evm.dmc.api.model.algorithm.SubAlgorithm;

public interface AlgorithmService {
	
	ProjectModel addDependentProject(Algorithm algorithm, ProjectModel project);
	
	
	static Algorithm getNewAlgorithm() {
		return new SubAlgorithm();
	}
	static Algorithm getNewFunction(FunctionModel function) {
		AlgorithmFunction alg =  new AlgorithmFunction();
		alg.setFunctionModel(function);
		return alg;
	}
}
