package evm.dmc.core.data;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public interface Data<T>{
	default String getDescription() {
		return "Unknown data";
	}

	T getData();

	void setData(T data);

	default boolean isMultiAttribute() {
		return false;
	}

}
