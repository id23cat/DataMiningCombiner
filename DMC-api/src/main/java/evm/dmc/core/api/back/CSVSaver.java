package evm.dmc.core.api.back;

import java.io.File;

import evm.dmc.core.api.DMCDataSaver;

public interface CSVSaver extends DMCDataSaver {
	CSVSaver setDestination(String filename);

	CSVSaver setDestination(File file);

}
