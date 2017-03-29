package evm.dmc.core.arithmetic;

import java.lang.NullPointerException;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.core.function.DMCFunction;

public abstract class AbstractArithmeticFunction<T/* extends Number*/> extends AbstractDMCFunction<T> {
	
	private Data<T> arg1 = null;
	private Data<T> arg2 = null;
	private Data<T> result = null;
	
	private BiFunction<Data<T>, Data<T>, Data<T>> function = null;
	
	@Autowired 
	ArithmeticContext context;
//	FrameworkContext context;
//	ArithmeticContext context = new ArithmeticContext();
	
	public AbstractArithmeticFunction(){	
	}
	
	@Autowired
	public AbstractArithmeticFunction(ArithmeticContext context){
		this.context = context;
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
	
	

	/* (non-Javadoc)
	 * @see evm.dmc.core.function.DMCFunction#addArgument(evm.dmc.core.data.Data)
	 */
	@Override
	public void addArgument(Data<T> arg) {
		if(arg1 == null)
			this.arg1 = arg;
		else if(arg2 == null)
			this.arg2 = arg;
		
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
	
	/* (non-Javadoc)
	 * @see evm.dmc.core.function.DMCFunction#getContext()
	 */
	@Override
	public FrameworkContext getContext() {		
		return context;
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.function.DMCFunction#setContext(evm.dmc.core.function.DMCFunction.FrameworkContext)
	 */	
	@Override
	public void setContext(FrameworkContext context) {
		this.context = (ArithmeticContext) context;
	}
	
//	public FrameworkContext<T> getNewContext(){
//		return new ArithmeticContext();
//	}
	
	@Override
	public void execute() {
		if( this.function == null)
			throw new NullPointerException("The operation is undefined");
//		this.setResult( function.apply(arg1, arg2) );
		context.executeInContext(this);
		
	}
		
	/**
	 * @author id23cat
	 * Context provides multiplier option for each arithmetic operation result 
	 */
	@Component
	public static class ArithmeticContext implements FrameworkContext{
		final private Integer defaultMultyplyer = 1;

		private Integer multiplier = defaultMultyplyer;
		
		@Autowired
		public ArithmeticContext(){
			this.multiplier = this.defaultMultyplyer;
		}
		
		public ArithmeticContext(Integer multyplyer) {
			this.multiplier = multyplyer;
		}
		
		@Override
		public void initContext() {
			this.multiplier = Integer.valueOf(defaultMultyplyer);
			
		}

		/**
		 * @return the multiplier
		 */
		public Integer getMultiplier() {
			return multiplier;
		}

		/**
		 * @param multiplier the multiplier to set
		 */
		public void setMultiplier(Integer multiplier) {
			this.multiplier = Integer.valueOf((Integer)multiplier);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void executeInContext(DMCFunction function) {
			AbstractArithmeticFunction func = (AbstractArithmeticFunction) function;
//			func.execute();
			func.result = (Data) func.function.apply(func.arg1, func.arg2);
			mutipyOn(func, multiplier);
			
		}
		
		@SuppressWarnings("unchecked")
		private <U extends Number> void mutipyOn(AbstractArithmeticFunction<U> func, Integer mul){
			if(func.result.getData() instanceof Double)
				func.result.setData((U) Double.valueOf(func.result.getData().doubleValue() * (double)mul.intValue()));
			else if(func.result.getData() instanceof Integer)
				func.result.setData((U) Integer.valueOf(func.result.getData().intValue() * mul.intValue()));
		}
		
	}

}
