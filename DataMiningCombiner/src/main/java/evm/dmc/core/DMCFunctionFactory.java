package evm.dmc.core;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;

public interface DMCFunctionFactory {
	DMCFunction getFunction(String descriptor);

	void addFramework(Framework framework);

	void initFactory();
}
