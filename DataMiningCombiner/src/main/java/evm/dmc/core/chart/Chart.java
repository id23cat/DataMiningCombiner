package evm.dmc.core.chart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import evm.dmc.core.data.Data;

public abstract class Chart implements Plotter {
	private static int WIDTH = 1024;
	private static int HEIGHT = 768;

	private BiFunction<Integer, Data, JFreeChart> chartKind;

	protected Chart() {
		this.chartKind = getChartKind();
	}

	public Chart(BiFunction<Integer, Data, JFreeChart> chartKind) {
		this.chartKind = chartKind;
	}

	// @PostConstruct
	public final void setChartKind(BiFunction<Integer, Data, JFreeChart> chartKind) {
		this.chartKind = chartKind;

	}

	/**
	 * Concrete implementation of Chart class must implemet this function to get
	 * concrete type of chart that would be used to expose data
	 * 
	 * @return Bi-argument function that accepts title for cart and data , and
	 *         returns JFreeChart object
	 */
	protected abstract BiFunction<Integer, Data, JFreeChart> getChartKind();

	/*
	 * Output file format: {prfix}_{data.title()}_{random value}.png
	 * 
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.chart.Plotter#saveToPng(evm.dmc.core.chart.Plottable,
	 * java.lang.String)
	 * 
	 */
	@Override
	public List<String> saveToPng(Data data, String prefix) throws IOException {
		List<String> files = new LinkedList<>();
		Random rnd = new Random(42);
		for (int index : data.getAttributesToPlot()) {
			JFreeChart chart = chartKind.apply(index, data);
			StringBuilder fname = new StringBuilder(prefix);
			fname.append("_");
			fname.append(data.getTitle(index));
			fname.append("_");
			fname.append(rnd.nextInt());
			fname.append(".png");
			ChartUtilities.saveChartAsPNG(new File(fname.toString()), chart, WIDTH, HEIGHT);
			files.add(fname.toString());
		}

		return files;
	}

	@Override
	public List<BufferedImage> getBufferedImage(Data data) {
		List<BufferedImage> images = new LinkedList<>();
		for (int index : data.getAttributesToPlot()) {
			JFreeChart chart = chartKind.apply(index, data);
			images.add(chart.createBufferedImage(WIDTH, HEIGHT));
		}
		return images;
	}

}
