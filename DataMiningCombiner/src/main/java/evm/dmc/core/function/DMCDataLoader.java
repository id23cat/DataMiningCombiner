package evm.dmc.core.function;

import evm.dmc.core.data.Data;
import evm.dmc.weka.exceptions.LoadDataException;

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
