package evm.dmc.core.api;

import evm.dmc.api.model.algorithm.Algorithm;

import java.io.IOException;

public interface AlgorithmExecutor {
    AlgorithmExecutor setAlgorithmModel(Algorithm algModel);

    AlgorithmExecutor execute() throws IOException;

    boolean getStatus();

}
