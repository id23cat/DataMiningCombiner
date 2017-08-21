package evm.dmc.core.api;

import evm.dmc.core.api.exceptions.LoadDataException;

public interface DMCDataLoader /* extends Supplier<Data> */ {
	/**
	 * Get data from source as single object
	 * 
	 * @return Data object contains loaded data
	 * @throws LoadDataException
	 */
	Data get() throws LoadDataException;

	/**
	 * Forces to reload data from beginning
	 */
	void restart();
}
