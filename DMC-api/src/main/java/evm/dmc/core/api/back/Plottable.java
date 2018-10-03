package evm.dmc.core.api.back;

/**
 * Interface allows implementations to be plotted on chart
 * 
 * @author id23cat
 */
public interface Plottable {
	/**
	 * Return values from some axe
	 *
	 * @param index		axe number
	 * @return			array of axe values
	 */
	default double[] plot(int index) {
		return new double[0];
	}

	/**
	 * Returns number of available axis
	 *
	 * @return	number of axis
	 */
	default int getElementsCount() {
		return 0;
	}

	/**
	 * Returns name of specified axe
	 *
	 * @param index		axe's index
	 * @return			axe's name
	 */
	default String getTitle(int index) {
		return "";
	}
}
