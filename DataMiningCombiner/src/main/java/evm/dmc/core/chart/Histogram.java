package evm.dmc.core.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import java.util.function.BiFunction;

public class Histogram extends Chart {
	static protected String xaxis = "number";
	static protected String yaxis = "value";
	static protected boolean show = false;
	static protected boolean toolTips = false;
	static protected boolean urls = false;
	static protected PlotOrientation orientation = PlotOrientation.VERTICAL;

	static private Histogram histSingletone;

	private Histogram() {
		super();
		// TODO Auto-generated constructor stub
	}

	static public Histogram getPlotter() {
		if (histSingletone == null)
			histSingletone = new Histogram();
		return histSingletone;
	}

	@Override
	protected BiFunction<String, Plottable, JFreeChart> getChartKind() {
		return (String title, Plottable data) -> {
			HistogramDataset dataset = new HistogramDataset();
			dataset.addSeries("Histogram", data.plot(), data.plot().length);
			return ChartFactory.createHistogram(title, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
		};
	}
}
