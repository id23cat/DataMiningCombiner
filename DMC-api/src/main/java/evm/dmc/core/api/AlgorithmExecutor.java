package evm.dmc.core.api;

import java.io.IOException;

import evm.dmc.api.model.AlgorithmModel;

public interface AlgorithmExecutor {
	AlgorithmExecutor setAlgorithmModel(AlgorithmModel algModel);
	AlgorithmExecutor execute() throws IOException;
	boolean getStatus();

}
