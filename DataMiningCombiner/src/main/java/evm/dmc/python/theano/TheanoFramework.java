package evm.dmc.python.theano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.core.FrameworkContext;
import evm.dmc.core.function.DMCFunction;

@Service
@TheanoFW
public class TheanoFramework extends AbstractFramework {
	@Autowired
	@TheanoFWContext
	private FrameworkContext frameworkContext;

	@Override
	public void initFramework() {
		// super.initFrameworkForType(AbstractTheanoFunction.class);

	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		// DMCFunction function = super.getDMCFunction(descriptor,
		// AbstractTheanoFunction.class);
		// function.setContext(frameworkContext);
		// return function;
		return null;
	}

}
