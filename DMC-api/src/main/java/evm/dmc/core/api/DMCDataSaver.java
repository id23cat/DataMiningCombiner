package evm.dmc.core.api;

import evm.dmc.core.api.exceptions.StoreDataException;

public interface DMCDataSaver {
	void save(Data data) throws StoreDataException;

}
