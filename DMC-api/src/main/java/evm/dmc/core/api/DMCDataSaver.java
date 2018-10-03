package evm.dmc.core.api;

import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.core.api.exceptions.StoreDataException;

/**
 * Common interface for data saving functionality. Represented as function at
 * persistence layer.
 */
public interface DMCDataSaver {

    /**
     * Sets the destination to save data to.
     *
     * @param filename      path to save data
     * @return              object itself
     */
    DMCDataSaver setDestination(String filename);

    /**
     * Saves data to some destination.
     *
     * @param data                      data to save
     * @throws StoreDataException       on any IO error
     * @throws IllegalStateException    if destination was not set
     */
	void save(Data<?> data) throws StoreDataException, IllegalStateException;

    /**
     * Returns the saving function model.
     *
     * @return      function model
     */
	FunctionDstModel getDstModel();

    /**
     * Sets the saving function model.
     *
     * @param model     function model
     * @return          object itself
     */
	DMCDataSaver setDstModel(FunctionDstModel model);
}
