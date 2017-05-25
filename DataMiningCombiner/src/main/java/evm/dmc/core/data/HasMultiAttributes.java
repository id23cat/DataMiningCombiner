package evm.dmc.core.data;

import evm.dmc.weka.exceptions.IndexOutOfRange;

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

	boolean isString(int column) throws IndexOutOfRange;

	boolean isDate(int column) throws IndexOutOfRange;

	boolean isNumeric(int column) throws IndexOutOfRange;

	int getIndexByName(String name) throws IndexOutOfRange;

	String getAttributeName(int column) throws IndexOutOfRange;

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
