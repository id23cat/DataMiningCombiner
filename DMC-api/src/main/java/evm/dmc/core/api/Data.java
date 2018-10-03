package evm.dmc.core.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.core.api.back.HasMultiAttributes;
import evm.dmc.core.api.back.Plottable;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;

/**
 * General interface for any DMC data.
 *
 * @param <T>       type of data
 * @see DataModel
 */
@Service
@Scope("prototype")
public interface Data<T> extends Plottable, HasMultiAttributes {
    /**
     * Returns value of data.
     *
     * @return      data
     */
    T getData();

    /**
     * Sets the value of data.
     *
     * @param data  data value to set
     */
    void setData(T data);

    /**
     * Returns description of data.
     *
     * @return      description
     */
	default String getDescription() {
		return "Unknown data";
	}


    /**
     * Checks if data has multiple attributes.
     *
     * @return      true is data has multiple attributes
     */
	default boolean isMultiAttribute() {
		return false;
	}

    /**
     * Converts all values to string.
     *
     * @return      string representation
     */
	String getAllAsString();

    /**
     * Returns the model of data.
     *
     * @return                          data model
     * @throws DataOperationException
     */
	DataModel getDataModel() throws DataOperationException;

    /**
     * Gets the preview of data model.
     *
     * @param previewRowsCount          number of rows in dataModel preview
     * @return
     * @throws DataOperationException
     */
	DataModel getDataModel(int previewRowsCount) throws DataOperationException;
}
