package evm.dmc.core.chart;

/**
 * Interface allows implementations to be plotted on chart
 * 
 * @author id23cat
 *
 */
public interface Plottable {
	double[] plot();

	String title();
}
