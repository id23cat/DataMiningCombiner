package evm.dmc.core.chart;

import java.io.IOException;

public interface Plotter {
	// void setChartKind(BiFunction<String, double[], JFreeChart> plot);

	/**
	 * @param data
	 *            to plot
	 * @param prefix
	 *            for a file
	 * @return full file name
	 * @throws IOException
	 */
	String saveToPng(Plottable data, String prefix) throws IOException;

	java.awt.image.BufferedImage getBufferedImage(Plottable data);

}
