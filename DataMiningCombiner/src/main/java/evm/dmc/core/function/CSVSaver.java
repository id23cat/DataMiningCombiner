package evm.dmc.core.function;

import java.io.File;

public interface CSVSaver extends DMCDataSaver {
	void setDestination(String filename);

	void setDestination(File file);

}
