package evm.dmc.core.function;

import java.util.function.Function;

import evm.dmc.core.data.Data;
/**
 * @author id23cat
 *
 */
public abstract class AbstractDMCFunction<T,U> implements DMCFunction {

	/**
	 * Name of the function, is used as key in map of functions presented in
	 * Framework collection
	 */
	protected String name;
	/**
	 * Count of the parameters have to be setted as arguments
	 */
	protected Integer paramCount;
	
	protected Function<Data<T>,Data<U>> function;
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
	public Integer getParamCount() {
		return paramCount;
	}
	/**
	 * @param paramCount the paramCount to set
	 */
	public void setParamCount(Integer paramCount) {
		this.paramCount = paramCount;
	}
	
//	public void setFunction(Function<Data<T>,Data<U>> func){
//		this.function = func;
//	}
	
//	public void execute(){
//		this.function.apply(this.get)
//		
//	}
	
	

}
