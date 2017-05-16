package evm.dmc.core.function;

import java.io.File;

public interface CSVSaver extends DMCDataSaver {
	CSVSaver setDestination(String filename);

	CSVSaver setDestination(File file);

}
