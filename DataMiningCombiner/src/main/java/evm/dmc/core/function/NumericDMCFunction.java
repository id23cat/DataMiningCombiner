package evm.dmc.core.function;

import evm.dmc.core.api.Data;

public abstract class NumericDMCFunction <T extends Number> extends AbstractDMCFunction {
	Data<T> result;

}
