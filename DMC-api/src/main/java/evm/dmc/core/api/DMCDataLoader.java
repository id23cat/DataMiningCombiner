package evm.dmc.core.api;

import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.core.api.exceptions.LoadDataException;

/**
 * Common interface for data loading functionality. Represented as function at
 * persistence layer.
 */
public interface DMCDataLoader /*extends Supplier<Data>*/ /* extends DMCFunction<T> */  {
	/**
	 * Get data from source as single object
	 * 
	 * @return Data object contains loaded data
	 * @throws LoadDataException
	 */
	Data<?> get() throws LoadDataException;

	/**
	 * Forces to reload data from beginning
	 */
	void restart();

    /**
     * Returns the model of loading function.
     *
     * @return      function model
     */
	FunctionSrcModel getSrcModel();

    /**
     * Sets the model of loading function.
     *
     * @param model     function model
     * @return          object itself
     */
	DMCDataLoader setSrcModel(FunctionSrcModel model);
	
	/**
	 * Set input file as String path in file system
	 * 
	 * @param source    string representation of the data source
     *                     (file or URL or smth else)
	 * @return          this object
	 */
	DMCDataLoader setSource(String source);
}
