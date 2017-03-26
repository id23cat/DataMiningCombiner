package evm.dmc.core.arithmetic;

import evm.dmc.core.NumericData;
import evm.dmc.core.data.Data;

public class InMemoryNumData <T extends Number> implements NumericData<T> {
	T data;
	
	public T getData(){
		return data;
	}
	
	public void setData(T data){
		this.data = data;
	}

}
