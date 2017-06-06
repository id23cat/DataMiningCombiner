package evm.dmc.core.chart;

import java.io.IOException;

import java.util.List;

import evm.dmc.core.HasNameAndDescription;
import evm.dmc.core.data.Data;

public interface Plotter extends HasNameAndDescription {
	/**
	 * Saves charts to files in PNG format
	 * 
	 * @param data
	 *            to plot
	 * @param prefix
	 *            for a file
	 * @return full file name
	 * @throws IOException
	 */
	List<String> saveToPng(Data data, String prefix) throws IOException;

	/**
	 * Get buffered image in memory
	 * 
	 * @param data
	 *            to plot
	 * @return object that contains chart image
	 */
	List<java.awt.image.BufferedImage> getBufferedImage(Data data);

}
