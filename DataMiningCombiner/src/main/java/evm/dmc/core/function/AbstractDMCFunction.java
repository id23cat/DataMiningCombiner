package evm.dmc.core.function;

import java.util.Arrays;
import java.util.List;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;

/**
 * @author id23cat
 *
 */
public abstract class AbstractDMCFunction<T> implements DMCFunction<T> {

	/**
	 * Name of the function, is used as key in map of functions presented in
	 * Framework collection
	 */
	// protected String description;
	/**
	 * Count of the parameters have to be setted as arguments
	 */
	protected Integer argsCount;

	protected List<Data<T>> arguments;

	public AbstractDMCFunction() {
		setArgsCount(getArgsCount());
		// this.description = funcName();

	}

	/**
	 * Each final implementation of AbstractDMCFunction must set count of
	 * accepted arguments
	 * 
	 * @param paramCount
	 *            the paramCount to set
	 */
	protected void setArgsCount(Integer paramCount) {
		this.argsCount = paramCount;
	}

	@SafeVarargs
	final public void setArgs(Data<T>... datas) {
		this.arguments = Arrays.asList(datas).subList(0, this.argsCount);
	}

	@Override
	public void setArgs(List<Data<T>> args) {
		this.arguments = args.subList(0, this.argsCount);
	}

	/**
	 * @return the arguments
	 */
	public List<Data<T>> getArguments() {
		return arguments;
	}

	/**
	 * Checks that all important properties filled correctly
	 *
	 * @return true, if successful
	 */
	protected boolean check() {

		if (argsCount == null || argsCount < 0)
			return false;
		if (arguments == null || arguments.size() != argsCount)
			return false;
		return true;
	}

}
