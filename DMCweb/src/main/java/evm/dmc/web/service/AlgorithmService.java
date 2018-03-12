package evm.dmc.web.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.AlgorithmFunction;
import evm.dmc.api.model.algorithm.SubAlgorithm;

public interface AlgorithmService {
	
	Optional<Algorithm> getByNameAndParentProject(String name, ProjectModel project);
	Optional<Algorithm> getByNameAndDependentProject(String name, ProjectModel project);
	Stream<Algorithm> getByNameAndParentProject(Set<String> names, ProjectModel project);
	Stream<Algorithm> getByNameAndDependentProject(Set<String> names, ProjectModel project);
	
	ProjectModel addDependentProject(Algorithm algorithm, ProjectModel project);
	
	void delete(Algorithm algorithm);
	Algorithm save(Algorithm algorithm);
	
	static Algorithm getNewAlgorithm() {
		return new SubAlgorithm();
	}
	static Algorithm getNewFunction(FunctionModel function) {
		AlgorithmFunction alg =  new AlgorithmFunction();
		alg.setFunctionModel(function);
		return alg;
	}
}
