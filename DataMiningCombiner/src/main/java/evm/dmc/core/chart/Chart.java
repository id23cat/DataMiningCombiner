package evm.dmc.core.chart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.util.function.BiFunction;

public abstract class Chart implements Plotter {
	private static int WIDTH = 1024;
	private static int HEIGHT = 768;

	private BiFunction<String, double[], JFreeChart> chartKind;

	Chart(BiFunction<String, double[], JFreeChart> chartKind) {
		this.chartKind = chartKind;
	}

	// @PostConstruct
	// public final void setChartKind() {
	// this.chartKind = getChartKind();
	// }
	//
	// protected abstract BiFunction<String, double[], JFreeChart>
	// getChartKind();

	@Override
	public void saveToPng(Plottable data, String prefix) throws IOException {
		JFreeChart chart = chartKind.apply(data.title(), data.plot());
		StringBuilder fname = new StringBuilder(prefix);
		fname.append("_");
		fname.append(data.title());
		fname.append(data.plot().length);
		fname.append(".png");
		ChartUtilities.saveChartAsPNG(new File(fname.toString()), chart, WIDTH, HEIGHT);
	}

	@Override
	public BufferedImage getBufferedImage(Plottable data) {
		JFreeChart chart = chartKind.apply(data.title(), data.plot());
		return null;
	}

}
