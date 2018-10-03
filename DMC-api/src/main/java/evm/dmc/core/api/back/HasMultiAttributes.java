package evm.dmc.core.api.back;

import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.core.api.exceptions.IndexOutOfRange;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;

/**
 * Indicates that implementation contains multiple attributes, such as
 * several columns in the data set.
 * 
 * @author id23cat
 */
public interface HasMultiAttributes {

	/**
	 * Returns count of attributes presented in current data set, is analog of
     * column in table view.
	 *
	 * @return number of attributes
	 */
	int getAttributesCount();

	/**
	 * Returns new Data instance contained single attribute selected by index
	 * from current multiAttribute data set.
	 * 
	 * @param index     index of attribute
	 * @return          new Data instance with single attribute at
     *                  specified index
	 */
	Data<?> getAttribute(int index) throws IndexOutOfRange;

    /**
     * Returns new Data instance contained single attribute selected by name
     * from current multiAttribute data set.
     *
     * @param name      name of attribute
     * @return          new Data instance with single attribute with
     *                  specified name
     */
	Data<?> getAttribute(String name) throws IndexOutOfRange;

	/**
	 * Returns new Data instance contained attributes selected by set of
	 * indexes from current multiAttribute data set.
	 * 
	 * @param indexes   indexes of attributes to include
	 * @return          new data instance
	 */
	Data<?> getAttributes(int... indexes) throws IndexOutOfRange;

    /**
     * Returns value of single item in data set.
     *
     * @param row               row number
     * @param column            attribute index
     * @return                  string value of item
     * @throws IndexOutOfRange  if row or column is out of range
     */
	String getValueAsString(int row, int column) throws IndexOutOfRange;

    /**
     * Returns value of single item in data set.
     *
     * @param row               row number
     * @param column            attribute index
     * @return                  double value of item
     * @throws IndexOutOfRange  if row or column is out of range
     */
	double getValue(int row, int column) throws IndexOutOfRange;

    /**
     * Converts all values of specified attribute to
     * {@link evm.dmc.core.api.AttributeType#NOMINAL}.
     *
     * @param column                    attribute index
     * @return                          nominal value of item
     * @throws IndexOutOfRange          if row or column is out of range
     * @throws DataOperationException   on conversion failure
     */
	void toNominal(int column) throws IndexOutOfRange, DataOperationException;

    /**
     * Checks if attribute is of {@link evm.dmc.core.api.AttributeType#NOMINAL}
     * type.
     *
     * @param column                    attribute index
     * @throws IndexOutOfRange          if attribute index is out of range
     * @throws DataOperationException
     */
    boolean isNominal(int column) throws IndexOutOfRange;

    /**
     * Checks if attribute is of {@link evm.dmc.core.api.AttributeType#STRING}
     * type.
     *
     * @param column                    attribute index
     * @throws IndexOutOfRange          if attribute index is out of range
     * @throws DataOperationException
     */
	boolean isString(int column) throws IndexOutOfRange;

    /**
     * Checks if attribute is of {@link evm.dmc.core.api.AttributeType#DATE}
     * type.
     *
     * @param column                    attribute index
     * @throws IndexOutOfRange          if attribute index is out of range
     * @throws DataOperationException
     */
	boolean isDate(int column) throws IndexOutOfRange;

	// void toDate(int column) throws IndexOutOfRange, DataOperationException;

    /**
     * Checks if attribute is of {@link evm.dmc.core.api.AttributeType#NUMERIC}
     * type.
     *
     * @param column                    attribute index
     * @throws IndexOutOfRange          if attribute index is out of range
     * @throws DataOperationException
     */
	boolean isNumeric(int column) throws IndexOutOfRange;

	// void toNumeric(int column) throws IndexOutOfRange,
	// DataOperationException;

    /**
     * Returns the index of attribute at specified name.
     *
     * @param name                    name of attribute
     * @throws IndexOutOfRange        if there is no attribute with such name
     */
	int getIndexByName(String name) throws IndexOutOfRange;

    /**
     * Returns the name of attribute at specified index.
     *
     * @param column                  index of attribute
     * @throws IndexOutOfRange        if column number is out of range
     */
	String getAttributeName(int column) throws IndexOutOfRange;

	/**
	 * Returns the statistics of attribute at specified index.
	 *
	 * @param column            attribute index
	 * @return                  attribute statistics
	 * @throws IndexOutOfRange  if index is out of range
	 */
	Statistics getAttributeStatistics(int column) throws IndexOutOfRange;

	/**
	 * Uses as alternative to default getAttributeStatistics(int column) when
	 * need to set number of bins in elements frequency for numeric attribute
	 * 
	 * @param column            attribute index
	 * @param bins              the number of bins (must be at least 1)
	 * @return
	 * @throws IndexOutOfRange  if index is out of range
	 */
	Statistics getAttributeStatistics(int column, int bins) throws IndexOutOfRange;

	/**
	 * Returns an enumeration of all the attribute's values if the attribute is
	 * nominal, string, or relation-valued, null otherwise.
	 * 
	 * @param column    index of attribute
	 * @return          enumeration of all the attribute's values
	 */
	java.util.Enumeration<java.lang.Object> enumerateValues(int column) throws IndexOutOfRange;

    /**
     * Returns an enumeration of all the attribute's values if the attribute is
     * nominal, string, or relation-valued, null otherwise.
     *
     * @param name      name of attribute
     * @return          enumeration of all the attribute's values
     */
	java.util.Enumeration<java.lang.Object> enumerateValues(String name) throws IndexOutOfRange;
}
