package evm.dmc.python;

import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;

@Service
@PythonFW
public class PythonFramework extends AbstractFramework {
	private static Class CLASS = AbstractPythonFunction.class;

	public PythonFramework() {
		// super(AbstractPythonFunction.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Framework#initFramework() Sets default abstract
	 * function type as AbstractPythonFunction.class
	 */
	@Override
	// @PostConstruct
	public void initFramework() {
		super.initFrameworkForType(CLASS);

	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		DMCFunction function = super.getDMCFunction(descriptor);
		return function;
	}

	@Override
	public Data getData(Class type) {
		return super.instantiateData(type);
	}

	@Override
	protected Class getFunctionClass() {
		return CLASS;
	}

}
