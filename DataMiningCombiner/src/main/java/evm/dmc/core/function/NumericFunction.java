package evm.dmc.core.function;

import evm.dmc.core.data.Data;

public abstract class NumericFunction <T extends Number> extends AbstractDMCFunction {
	Data<T> result;

}
