package evm.dmc.web.service;


import java.util.List;
import java.util.Optional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.exceptions.MetaDataNotFoundException;
import evm.dmc.web.service.dto.TreeNodeDTO;

public interface AlgorithmService extends EnityGetter<Algorithm>, EntityModifier<Algorithm>{
	
	
	Algorithm addNew(ProjectModel project, Algorithm algorithm);
	Algorithm setDataSource(Algorithm algorithm, String datasetName) throws MetaDataNotFoundException;
//	MetaData getDataSource(Algorithm algorithm);
	Optional<MetaData> getDataSource(Optional<Algorithm> optAlgorithm);
	
	Algorithm setAttributes(Algorithm algorithm, MetaData metaData);
	
	/**
	 * Adds function at the end of functions queue
	 * @param algorithm
	 * @param dtoFunction
	 * @return
	 */
	FWMethod addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction);
	FWMethod addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step);
	
	List<FrameworkModel> getFrameworksList();
	List<TreeNodeDTO> getFrameworksAsTreeNodes();
	
	static Algorithm getNewAlgorithm(ProjectModel project) {
		return Algorithm.builder().project(project).build();
	}
	
	static FWMethod functionToFWMethod(FunctionModel function) {
		return FWMethod.builder()
			.name(function.getName())
			.description(function.getDescription())
			.frameworkFunction(function)
			.properties(function.getProperties())
			.build();
	}
	
	static PatternMethod algorithmCreatePatternMethod(Algorithm algorithm) {
		PatternMethod pattern = PatternMethod.patternBuilder()
				.name(algorithm.getName())
				.description(algorithm.getDescription())
				.dependentAlgorithm(algorithm)
				.build();
//		pattern.getDependentAlgorithms().add(algorithm);
		algorithm.setMethod(pattern);
		return pattern;
	}
	
//	static Algorithm getNewFunction(FunctionModel function) {
//		FWFunction alg =  new FWFunction();
//		alg.setFunctionModel(function);
//		return alg;
//	}
}
