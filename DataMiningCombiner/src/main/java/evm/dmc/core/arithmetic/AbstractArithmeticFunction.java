package evm.dmc.core.arithmetic;

import java.lang.NullPointerException;
import java.util.function.BiFunction;

import evm.dmc.core.data.Data;
import evm.dmc.core.function.AbstractDMCFunction;

public abstract class AbstractArithmeticFunction<T> extends AbstractDMCFunction {
	
	private Data<T> arg1 = null;
	private Data<T> arg2 = null;
	private Data<T> result = null;
	
	private BiFunction<Data<T>, Data<T>, Data<T>> function = null;
	
	public AbstractArithmeticFunction(){		
	}
	
	public AbstractArithmeticFunction(BiFunction<Data<T>, Data<T>, Data<T>> function){
		setFunction(function);
	}
	
	public AbstractArithmeticFunction(Data<T> arg1, Data<T> arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public AbstractArithmeticFunction(Data<T> arg1, Data<T> arg2, BiFunction<Data<T>, Data<T>, Data<T>> function) {
		super();
		setFunction(function);
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public void setArgs(Data<T> arg1, Data<T> arg2){
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	/**
	 * @return the arg1
	 */
	public Data<T> getArg1() {
		if( this.arg1 == null)
			throw new NullPointerException("The argument1 of operation is undefined");
		return this.arg1;
	}

	/**
	 * @param arg1 the arg1 to set
	 */
	public void setArg1(Data<T> arg1) {
		this.arg1 = arg1;
	}

	/**
	 * @return the arg2
	 */
	public Data<T> getArg2() {
		if( this.arg2 == null)
			throw new NullPointerException("The argument2 of operation is undefined");
		return this.arg2;
	}

	/**
	 * @param arg2 the arg2 to set
	 */
	public void setArg2(Data<T> arg2) {
		this.arg2 = arg2;
	}

	/**
	 * @return the result
	 */
	public Data<T> getResult() {
		if( this.result == null)
			throw new NullPointerException("The result is undefined");
		return this.result;
	}
	
	protected void setResult(Data<T> result) {
		this.result = result;
	}

	/**
	 * @return the function
	 */
	public BiFunction<Data<T>, Data<T>, Data<T>> getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(BiFunction<Data<T>, Data<T>, Data<T>> function) {
		this.function = function;
	}
	
	@Override
	public void execute() {
		if( this.result == null)
			throw new NullPointerException("The operation is undefined");
		this.setResult( function.apply(arg1, arg2) );
		
	}

}
