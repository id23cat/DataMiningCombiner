package evm.dmc.core.function;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.List;

import evm.dmc.core.data.Data;

@Scope("prototype")
@Lazy
public interface DMCFunction<T> {
	void execute();

	default String getName() {
		return "Warning: No name specified";
	}

	Integer getArgsCount();

	void setName(String name);

	// void setArgsCount(Integer count);

	// @Deprecated
	// void addArgument(Data<T> arg);

	@SuppressWarnings("unchecked")
	void setArgs(Data<T>... datas);

	void setArgs(List<Data<T>> largs);

	Data<T> getResult();

}
