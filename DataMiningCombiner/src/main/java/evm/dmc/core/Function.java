package evm.dmc.core;

import java.util.List;

public interface Function {
	void execute();

	String getName();

	Integer getParamCount();

	NumericData getResult();

	void setDataArguments(List<NumericData> data);

	void setName(String name);

//	void setParamCount(Integer paramCount);

}
