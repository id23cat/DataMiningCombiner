package evm.dmc.core.api;

import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.api.back.CSVSaver;
import evm.dmc.core.api.exceptions.StoreDataException;

public interface DMCDataSaver {
	void save(Data data) throws StoreDataException;
	
	FunctionDstModel getDstModel();
	
	DMCDataSaver setDstModel(FunctionDstModel model);
	
	DMCDataSaver setDestination(String filename);
}
