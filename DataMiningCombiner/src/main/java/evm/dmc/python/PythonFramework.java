package evm.dmc.python;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.python.function.AbstractPythonFunction;

@Service
@PythonFW
public class PythonFramework extends AbstractFramework {

	@Autowired
	@PythonFWContext
	FrameworkContext context;

	private static Class CLASS = AbstractPythonFunction.class;

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Framework#initFramework() Sets default abstract
	 * function type as AbstractPythonFunction.class
	 */
	@Override
	public void initFramework() {

	}

	public PythonFramework() {
	}

	@Override
	public Data getData(Class type) {
		return super.instantiateData(type);
	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		DMCFunction function = super.getDMCFunction(descriptor);
		function.setContext(context);
		return function;
	}

	@Override
	protected Class getFunctionClass() {
		return CLASS;
	}

}
