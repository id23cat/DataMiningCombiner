package evm.dmc.core.function;


public interface DMCFunction {
	void execute();

	String getName();

	Integer getArgsCount();
	
	void setName(String name);
	
	void setArgsCount(Integer count);
	
//	Data<T> getResult;

}
