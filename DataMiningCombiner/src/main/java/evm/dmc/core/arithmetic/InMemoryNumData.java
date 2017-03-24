package evm.dmc.core.arithmetic;

import evm.dmc.core.Data;
import evm.dmc.core.NumericData;

public class InMemoryNumData <T extends Number> implements NumericData<T> {
	T data;
	
	public T getData(){
		return data;
	}
	
	public void setData(T data){
		this.data = data;
	}

}
