package evm.dmc.core.function;

/**
 * @author id23cat
 *
 */
public abstract class AbstractDMCFunction<T> implements DMCFunction<T> {

	/**
	 * Name of the function, is used as key in map of functions presented in
	 * Framework collection
	 */
	protected String name;
	/**
	 * Count of the parameters have to be setted as arguments
	 */
	protected Integer argsCount;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the paramCount
	 */
	public Integer getArgsCount() {
		return argsCount;
	}
	/**
	 * @param paramCount the paramCount to set
	 */
	public void setArgsCount(Integer paramCount) {
		this.argsCount = paramCount;
	}

	

}
