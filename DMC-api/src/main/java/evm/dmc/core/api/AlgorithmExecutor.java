package evm.dmc.core.api;

import java.io.IOException;

import evm.dmc.api.model.algorithm.Algorithm;

/**
 * Allows to execute algorithms from some algorithm model.
 *
 * @see Algorithm
 */
public interface AlgorithmExecutor {
    /**
     * Sets the algorithm model to execute.
     *
     * @param algModel      algorithm model
     * @return              object itself
     */
	AlgorithmExecutor setAlgorithmModel(Algorithm algModel);

    /**
     * Executes configured model.
     *
     * @return              object itself
     * @throws IOException
     */
	AlgorithmExecutor execute() throws IOException;

    /**
     *
     *
     * @return
     */
	boolean getStatus();
}
