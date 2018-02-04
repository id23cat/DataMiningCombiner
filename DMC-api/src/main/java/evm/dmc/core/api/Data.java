package evm.dmc.core.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.core.api.back.HasMultiAttributes;
import evm.dmc.core.api.back.Plottable;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;

@Service
@Scope("prototype")
public interface Data<T> extends Plottable, HasMultiAttributes {
	default String getDescription() {
		return "Unknown data";
	}

	T getData();

	void setData(T data);

	default boolean isMultiAttribute() {
		return false;
	}

	String getAllAsString();

	DataModel getDataModel() throws DataOperationException;

	DataModel getDataModel(Integer previewRowsCount) throws DataOperationException;
}
