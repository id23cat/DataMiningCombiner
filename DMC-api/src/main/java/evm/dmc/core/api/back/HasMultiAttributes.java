package evm.dmc.core.api.back;

import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.core.api.exceptions.IndexOutOfRange;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;

/**
 * Indicates that implementation contains multiple attributes, such as
 * several columns in the dataset.
 * 
 * @author id23cat
 * 
 * @param <T>
 */
public interface HasMultiAttributes {

	/**
	 * @return count of attributes presented in current data set, is analog of
	 *         column in table view
	 */
	int getAttributesCount();

	/**
	 * Returns new Data instance contained single attribute selected by index
	 * from current multiAttribute dataset.
	 * 
	 * @param index
	 * @return
	 */
	Data<?> getAttribute(int index) throws IndexOutOfRange;

	Data<?> getAttribute(String name) throws IndexOutOfRange;

	/**
	 * Returns new Data instance contained single attribute selected by set of
	 * indexes
	 * from current multiAttribute dataset.
	 * 
	 * @param indexes
	 * @return
	 */
	Data<?> getAttributes(int... indexes) throws IndexOutOfRange;

	String getValueAsString(int row, int column) throws IndexOutOfRange;

	double getValue(int row, int column) throws IndexOutOfRange;

	boolean isNominal(int column) throws IndexOutOfRange;

	void toNominal(int column) throws IndexOutOfRange, DataOperationException;

	boolean isString(int column) throws IndexOutOfRange;

	boolean isDate(int column) throws IndexOutOfRange;

	// void toDate(int column) throws IndexOutOfRange, DataOperationException;

	boolean isNumeric(int column) throws IndexOutOfRange;

	// void toNumeric(int column) throws IndexOutOfRange,
	// DataOperationException;

	int getIndexByName(String name) throws IndexOutOfRange;

	String getAttributeName(int column) throws IndexOutOfRange;

	Statistics getAttributeStatistics(int column) throws IndexOutOfRange;

	/**
	 * Uses as alternative to default getAttributeStatistics(int column) when
	 * need to set number of bins in elements frequency for numeric attribute
	 * 
	 * @param column
	 * @param bins
	 *            - the number of bins (must be at least 1)
	 * @return
	 * @throws IndexOutOfRange
	 */
	Statistics getAttributeStatistics(int column, int bins) throws IndexOutOfRange;

	/**
	 * Returns an enumeration of all the attribute's values if the attribute is
	 * nominal, string, or relation-valued, null otherwise.
	 * 
	 * @param column
	 *            index of attribute
	 * @return enumeration of all the attribute's values
	 */
	java.util.Enumeration<java.lang.Object> enumerateValues(int column) throws IndexOutOfRange;

	java.util.Enumeration<java.lang.Object> enumerateValues(String name) throws IndexOutOfRange;
}
