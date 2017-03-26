package evm.dmc.core.function;

import java.util.function.Function;

import evm.dmc.core.data.Data;

public interface DMC2ArgFunction<T> extends DMCFunction{
	T execute(Data<T> data1, Data<T> data2);
	T execute(Function<T,T> function, Data<T> data);
	
	@Override
	default Integer getArgsCount() {return 2;}
}
