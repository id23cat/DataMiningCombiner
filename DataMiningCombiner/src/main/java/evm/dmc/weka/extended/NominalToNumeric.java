package evm.dmc.weka.extended;

import weka.core.Instances;
import weka.filters.SimpleBatchFilter;

public class NominalToNumeric extends SimpleBatchFilter {

	/**
	 * Returns a string describing this filter
	 * 
	 * @return a description of the filter suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	@Override
	public String globalInfo() {
		return "A filter for turning nominal attributes into numeric ones. Useful after CSV "
				+ "imports, to enforce certain attributes to become Numeric.";
	}

	/**
	 * Determines the output format based on the input format and returns this.
	 * In
	 * case the output format cannot be returned immediately, i.e.,
	 * immediateOutputFormat() returns false, then this method will be called
	 * from
	 * batchFinished().
	 * 
	 * @param inputFormat
	 *            the input format to base the output format on
	 * @return the output format
	 * @throws Exception
	 *             in case the determination goes wrong
	 * @see #hasImmediateOutputFormat()
	 * @see #batchFinished()
	 */
	@Override
	protected Instances determineOutputFormat(Instances inputFormat) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Instances process(Instances instances) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
