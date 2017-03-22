package evm.dmc.core;

public interface Command {
	void setData(Data data);
	void execute();
	void getResult();

}
