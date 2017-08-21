package evm.dmc.python.theano;

import javax.annotation.PostConstruct;

import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.FrameworkContext;
import evm.dmc.core.function.AbstractDMCFunction;

public abstract class AstractTheanoFunction<T> extends AbstractDMCFunction<T> {

	@Service
	@TheanoFWContext
	public static class TheanoContext implements FrameworkContext {
		@Autowired
		PythonInterpreter python;

		@Override
		@PostConstruct
		public void initContext() {
			// TODO Auto-generated method stub

		}

		@Override
		public void executeInContext(DMCFunction function) {
			AstractTheanoFunction func = (AstractTheanoFunction) function;
			func.result = (Data) func.function.apply(func.arguments.get(0), func.arguments.get(1));

		}

		@Override
		public void getValue(DMCFunction function) {
			// TODO Auto-generated method stub

		}

	}

	private Data<T> result = null;

	private BiFunction<Data<T>, Data<T>, Data<T>> function = null;

	@Autowired
	@TheanoFWContext
	FrameworkContext context;

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public Data<T> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FrameworkContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(FrameworkContext context) {
		// TODO Auto-generated method stub

	}

}
