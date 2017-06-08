package evm.dmc.core;

import evm.dmc.core.function.DMCFunction;

public interface DMCFunctionFactory {
	DMCFunction getFunction(String descriptor);

	void addFramework(Framework framework);

	void initFactory();
}
