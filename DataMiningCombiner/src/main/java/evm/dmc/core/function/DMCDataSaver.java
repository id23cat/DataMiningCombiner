package evm.dmc.core.function;

import evm.dmc.core.data.Data;
import evm.dmc.core.exceptions.StoringException;

public interface DMCDataSaver {
	void save(Data data) throws StoringException;

}
