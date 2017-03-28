package evm.dmc.core.function;


public interface DMCFunction {
	void execute();

	default String getName() {return "Warning: No name specified";}

	Integer getArgsCount();
	
	void setName(String name);
	
	void setArgsCount(Integer count);
	
//	Data<T> getResult;

}
