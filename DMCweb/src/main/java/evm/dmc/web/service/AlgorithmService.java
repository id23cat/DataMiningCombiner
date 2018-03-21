package evm.dmc.web.service;

import java.util.Optional;
import java.util.Set;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;

public interface AlgorithmService {
	
	Optional<Algorithm> getByNameAndProject(String name, ProjectModel project);
	Set<Algorithm> getByNameAndProject(Set<String> names, ProjectModel project);
	
	Set<Algorithm> getForProject(ProjectModel project);
	
	void delete(ProjectModel project, Set<String> algorithmNames);
	void delete(Algorithm algorithm);
	Algorithm addNew(ProjectModel project, Algorithm algorithm);
	Algorithm save(Algorithm algorithm);
	
	static Algorithm getNewAlgorithm() {
		return new Algorithm();
	}
//	static Algorithm getNewFunction(FunctionModel function) {
//		FWFunction alg =  new FWFunction();
//		alg.setFunctionModel(function);
//		return alg;
//	}
}
