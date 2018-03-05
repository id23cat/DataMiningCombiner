package evm.dmc.core.api;

import java.io.IOException;

import evm.dmc.api.model.algorithm.Algorithm;

public interface AlgorithmExecutor {
	AlgorithmExecutor setAlgorithmModel(Algorithm algModel);
	AlgorithmExecutor execute() throws IOException;
	boolean getStatus();

}
