package evm.dmc.core.arithmetic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.core.function.DMCFunction;

public abstract class AbstractArithmeticFunction<T/* extends Number */> extends AbstractDMCFunction<T> {

	/**
	 * @author id23cat Context provides multiplier option for each arithmetic
	 *         operation result
	 */
	@Service
	@ArithmeticFWContext
	// @Scope("prototype")
	public static class ArithmeticContext implements FrameworkContext {
		final private Integer defaultMultyplyer = 1;

		// @Autowired
		// @ArithmeticFW
		// private Framework framework;

		private Integer multiplier = defaultMultyplyer;

		@Autowired
		public ArithmeticContext() {
			initContext();
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
		 * @param multiplier
		 *            the multiplier to set
		 */
		public void setMultiplier(Integer multiplier) {
			this.multiplier = Integer.valueOf(multiplier);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void executeInContext(DMCFunction function) {
			AbstractArithmeticFunction func = (AbstractArithmeticFunction) function;
			// func.execute();
			func.result = (Data) func.function.apply(func.arguments.get(0), func.arguments.get(1));
			mutipyOn(func, multiplier);

		}

		// @Override
		// public Data getNewData() {
		// Data data = framework.getData(0);
		// return data;
		// }

		@SuppressWarnings("unchecked")
		private <U extends Number> void mutipyOn(AbstractArithmeticFunction<U> func, Integer mul) {
			if (func.result.getData() instanceof Double)
				func.result.setData((U) Double.valueOf(func.result.getData().doubleValue() * mul.intValue()));
			else if (func.result.getData() instanceof Integer)
				func.result.setData((U) Integer.valueOf(func.result.getData().intValue() * mul.intValue()));
		}

		@Override
		public void getValue(DMCFunction function) {
			AbstractArithmeticFunction func = (AbstractArithmeticFunction) function;
			func.getResult().setData(func.arguments.get(0));

		}

	}

	// private Data<T> arg1 = null;
	// private Data<T> arg2 = null;
	private Data<T> result = null;

	private BiFunction<Data<T>, Data<T>, Data<T>> function = null;

	@Autowired
	@ArithmeticFWContext
	FrameworkContext context;

	public AbstractArithmeticFunction() {
	}

	@Autowired
	public AbstractArithmeticFunction(ArithmeticContext context) {
		this.context = context;
	}

	public AbstractArithmeticFunction(BiFunction<Data<T>, Data<T>, Data<T>> function) {
		setFunction(function);
	}

	public AbstractArithmeticFunction(Data<T> arg1, Data<T> arg2) {
		super();
		this.setArgs(arg1, arg2);
	}

	public AbstractArithmeticFunction(Data<T> arg1, Data<T> arg2, BiFunction<Data<T>, Data<T>, Data<T>> function) {
		super();
		setFunction(function);
		this.setArgs(arg1, arg2);
	}

	public void setArgs(Data<T> arg1, Data<T> arg2) {
		this.setArgs(arg1, arg2);
	}

	/**
	 * @return the result
	 */
	@Override
	public Data<T> getResult() {
		if (this.result == null)
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
	 * @param function
	 *            the function to set
	 */
	public void setFunction(BiFunction<Data<T>, Data<T>, Data<T>> function) {
		this.function = function;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.function.DMCFunction#getContext()
	 */
	@Override
	public FrameworkContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.function.DMCFunction#setContext(evm.dmc.core.function.
	 * DMCFunction.FrameworkContext)
	 */
	@Override
	public void setContext(FrameworkContext context) {
		this.context = context;
	}

	// public FrameworkContext<T> getNewContext(){
	// return new ArithmeticContext();
	// }

	@Override
	public void execute() {
		if (!check())
			throw new NullPointerException("The operation is undefined");
		// this.setResult( function.apply(arg1, arg2) );
		context.executeInContext(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.function.AbstractDMCFunction#check()
	 */
	@Override
	protected boolean check() {
		if (context == null)
			return false;
		if (function == null)
			return false;

		return super.check();
	}

}
