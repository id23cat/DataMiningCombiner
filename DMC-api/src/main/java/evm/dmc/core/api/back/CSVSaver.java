package evm.dmc.core.api.back;

import java.io.File;

import evm.dmc.core.api.DMCDataSaver;

/**
 * Allows to save data in csv format.
 */
public interface CSVSaver extends DMCDataSaver {
    /**
     * Sets the destination to save data.
     *
     * @param filename      path to save data
     * @return              object itself
     */
    CSVSaver setDestination(String filename);

    /**
     * Sets the destination to save data.
     *
     * @param file          file to save data to
     * @return              object itself
     */
	CSVSaver setDestination(File file);

}
