package evm.dmc.core.function;

import evm.dmc.core.data.Data;
import evm.dmc.weka.exceptions.LoadDataException;

public interface DMCDataLoader /* extends Supplier<Data> */ {
	Data get() throws LoadDataException;
}
