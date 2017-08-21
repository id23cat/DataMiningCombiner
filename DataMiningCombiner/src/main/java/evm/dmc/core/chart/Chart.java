package evm.dmc.core.chart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import evm.dmc.core.api.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public abstract class Chart implements Plotter {
	private int width = 1024;
	private int height = 768;

	private int[] selectedAttributes;

	private BiFunction<Integer, Data, JFreeChart> chartKind;

	protected Chart() {
		this.chartKind = getChartKind();
	}

	public Chart(BiFunction<Integer, Data, JFreeChart> chartKind) {
		this.chartKind = chartKind;
	}

	/**
	 * @return the width
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	@Override
	public void setHeight(int height) {
		this.height = height;
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
		for (int index : selectedAttributes) {
			JFreeChart chart = chartKind.apply(index, data);
			StringBuilder fname = new StringBuilder(prefix);
			fname.append("_");
			fname.append(data.getTitle(index));
			fname.append("_");
			fname.append(rnd.nextInt());
			fname.append(".png");
			ChartUtilities.saveChartAsPNG(new File(fname.toString()), chart, width, height);
			files.add(fname.toString());
		}

		return files;
	}

	@Override
	public List<BufferedImage> getBufferedImage(Data data) {
		List<BufferedImage> images = new LinkedList<>();
		for (int index : selectedAttributes) {
			JFreeChart chart = chartKind.apply(index, data);
			images.add(chart.createBufferedImage(width, height));
		}
		return images;
	}

	@Override
	public Chart setAttribIndexesToPlot(int... indexes) {
		selectedAttributes = indexes.clone();
		return this;
	}

}
