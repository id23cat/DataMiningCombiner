package evm.dmc.core.function;

import evm.dmc.core.data.Data;

public interface DFMC1ArgFunction<T> extends DMCFunction{
	T execute(Data<T> data);
	
	@Override
	default Integer getArgsCount() {return 1;}

}
