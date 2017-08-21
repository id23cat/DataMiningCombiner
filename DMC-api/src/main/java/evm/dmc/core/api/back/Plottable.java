package evm.dmc.core.api.back;

/**
 * Interface allows implementations to be plotted on chart
 * 
 * @author id23cat
 *
 */
public interface Plottable {
	default double[] plot(int index) {
		return null;
	}

	default int getElementsCount() {
		return -1;
	}

	default String getTitle(int index) {
		return null;
	}
}
