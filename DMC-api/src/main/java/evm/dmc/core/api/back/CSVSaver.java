package evm.dmc.core.api.back;

import evm.dmc.core.api.DMCDataSaver;

import java.io.File;

public interface CSVSaver extends DMCDataSaver {
    CSVSaver setDestination(String filename);

    CSVSaver setDestination(File file);

}
