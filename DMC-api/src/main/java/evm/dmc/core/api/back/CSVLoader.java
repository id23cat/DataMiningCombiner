package evm.dmc.core.api.back;

import evm.dmc.core.api.DMCDataLoader;

/**
 * Allows to load data from csv format.
 */
public interface CSVLoader extends DMCDataLoader {
	/**
	 * Set input file as String path in file system
	 * 
	 * @param filename  file name to load from
	 * @return          this object
	 */
	CSVLoader setSource(String filename);

	/**
	 * Whether file has header line
	 * 
	 * @param b
	 *            - true if it has
	 * @return      this object
	 */
	CSVLoader hasHeader(boolean b);

	/**
	 * Interpret given attribute as Date type
	 * 
	 * @param index     index of attribute starting from 0
	 * @return          this object
	 */
	CSVLoader asDate(int index);

	/**
	 * Set input Date format
	 * Example: "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * @param format        data format
	 * @return              this object
	 */
	CSVLoader setDateFormat(String format);

	/**
	 * Interpret given attribute as Numeric type
	 * 
	 * @param index     attribute index
	 * @return          this object
	 */
	CSVLoader asNumeric(int index);

	/**
	 * Interpret given attribute as Nominal type
	 * 
	 * @param index     attribute index
	 * @return          this object
	 */
	CSVLoader asNominal(int index);

	/**
	 * Interpret given attribute as String type
	 * 
	 * @param index     attribute index
	 * @return          this object
	 */
	CSVLoader asString(int index);
}
