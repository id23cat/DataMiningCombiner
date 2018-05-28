package evm.dmc.web.service;

import java.util.Optional;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.web.service.dto.TreeNodeDTO;

public interface MethodService {
	/**
	 * Adds function at the end of functions queue
	 * @param algorithm
	 * @param dtoFunction
	 * @return
	 */
	Algorithm addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction);
	Algorithm addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step);
	Algorithm setMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step);
	
	/** Returns method from the specified step in algorithm.method.steps List 
	 * @param algorithm
	 * @param step
	 * @return
	 */
	PatternMethod getStep(Algorithm algorithm, Integer step);
	
	Optional<PatternMethod> getMethod(Algorithm algorithm, Long id);
	
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

}
