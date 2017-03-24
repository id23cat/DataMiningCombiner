package evm.dmc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author id23cat
 *
 */
public abstract class AbstactFunction implements Function {

	/**
	 * Name of the function, is used as key in map of functions presented in
	 * Framework collection
	 */
	protected String name;
	/**
	 * Count of the parameters have to be setted as arguments
	 */
	protected Integer paramCount;

	/**
	 * Parameters that would be setted as arguments to function
	 */
	protected List<NumericData> dataArguments;

	
	/**
	 * Contains the result of the function execution
	 */
	protected NumericData result;

	/**
	 * @return the dataArguments
	 */
	public List<?> getDataArguments() {
		return dataArguments;
	}

	/**
	 * @param dataArguments
	 *            the dataArguments to set
	 */
	public void setDataArguments(List<NumericData> dataArguments) {
		this.dataArguments = dataArguments;
	}
	
	public void setDataArguments(NumericData... dataArguments){
		this.dataArguments = Arrays.asList(dataArguments);
	}

	/**
	 * @return the dataResult
	 */
	public NumericData getResult() {
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getParamCount() {
		return paramCount;
	}

}
