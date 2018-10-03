package evm.dmc.core.api;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.api.back.FrameworkContext;
import evm.dmc.core.api.back.HasNameAndDescription;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Optional;

/**
 * Contains common methods that every DMC function must implement.
 *
 * @param <T>       type of data to work with
 */
@Scope("prototype")
@Lazy
public interface DMCFunction<T extends Data<?>> extends HasNameAndDescription {
    /**
     * Stats function execution.
     */
    void execute();

    /**
     * Returns the number of function arguments.
     *
     * @return      number of attributes
     */
    int getArgsCount();

    // void setName(String name);

    // void setArgsCount(Integer count);

    // @Deprecated
    // void addArgument(Data<T> arg);

    /**
     * Sets function arguments as array.
     *
     * @param datas     array of function arguments
     */
    @SuppressWarnings("unchecked")
    void setArgs(Data<T>... datas);

    /**
     * Sets function arguments as list.
     *
     * @param largs     list of function arguments
     */
    void setArgs(List<Data<T>> largs);

    /**
     * Returns the result of function execution.
     *
     * @return                              execution result
     * @throws      IllegalStateException   if function was not executed yet
     */
    Data<T> getResult() throws IllegalStateException;

    /**
     * Returns the result of function execution as an optional value.
     *
     * @return                              execution result
     * @throws      IllegalStateException   if function was not executed yet
     */
    Optional<Data<T>> getOptionalResult();

    /**
     * Returns object that should be used in function object as context of
     * execution
     *
     * @return      context object
     */
    default FrameworkContext getContext() {
        return null;
    }

    /**
     * Sets object that should be used in function object as context of
     * execution
     */
    default void setContext(FrameworkContext context) {

    }

    /**
     * Sets the model object for this function.
     *
     * @param model     model object
     * @return          function itself
     */
    DMCFunction<T> setFunctionModel(FunctionModel model);

    /**
     * Returns the model of this function.
     *
     * @return      function itself
     */
    FunctionModel getFunctionModel();

    /**
     * Returns the model of invocation result.
     *
     * @return                          data model
     * @throws DataOperationException
     * @throws IllegalStateException    if function was not executed yet
     */
    DataModel getResultDataModel() throws DataOperationException;

    /**
     * Returns the model of invocation result with limited preview.
     *
     * @param previewRowsCount          number of rows in data preview
     * @return                          data model
     * @throws DataOperationException
     * @throws IllegalStateException    if function was not executed yet
     */
    DataModel getResultDataModel(Integer previewRowsCount) throws DataOperationException;
}
