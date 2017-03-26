package evm.dmc.core.data;

public interface Data<T> {
	default String getDescription() { return "Unknown data";}
	
	T getData();
	void setData(T data);

}
