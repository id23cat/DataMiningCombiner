package evm.dmc.core.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;

@Service(ChartTypes.HISTOGRAM)
@PropertySource("classpath:chart.properties")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Histogram extends Chart {
	private final static String NAME = ChartTypes.HISTOGRAM;

	@Value("${chart.hist_desc}")
	private String description;
	static protected String xaxis = "number";
	static protected String yaxis = "value";
	static protected boolean show = false;
	static protected boolean toolTips = false;
	static protected boolean urls = false;
	static protected PlotOrientation orientation = PlotOrientation.VERTICAL;

	// static private Histogram histSingletone;

	// private Histogram() {
	public Histogram() {
		super();
	}

	// static public Histogram getPlotter() {
	// if (histSingletone == null)
	// histSingletone = new Histogram();
	// return histSingletone;
	// }

	@Override
	protected BiFunction<Integer, Data, JFreeChart> getChartKind() {
		return (Integer index, Data data) -> {
			if (data.isNumeric(index)) {
				HistogramDataset dataset = new HistogramDataset();
				dataset.setType(HistogramType.FREQUENCY);
				double[] toPlot = data.plot(index);
				dataset.addSeries("Histogram", toPlot, toPlot.length);
				return ChartFactory.createHistogram(data.getTitle(index), xaxis, yaxis, dataset, orientation, show,
						toolTips, urls);
			} else {
				// Map<String, Integer> map = new HashMap<>();
				// for (int i = 0; i < data.getElementsCount(); i++) {
				// data.getValueAsString(i, index);
				// }
				// // for()
				Statistics stat = data.getAttributeStatistics(index);
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				stat.getMapValuesCount().forEach((key, value) -> dataset.addValue(value, "Count", (Comparable) key));
				return ChartFactory.createBarChart(stat.getName(), "Value", "Count", dataset, PlotOrientation.VERTICAL,
						true, true, false);
			}
		};
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
