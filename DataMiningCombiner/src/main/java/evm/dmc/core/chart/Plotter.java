package evm.dmc.core.chart;

import java.io.IOException;

public interface Plotter {
	// void setChartKind(BiFunction<String, double[], JFreeChart> plot);

	void saveToPng(Plottable data, String prefix) throws IOException;

	java.awt.image.BufferedImage getBufferedImage(Plottable data);

}
