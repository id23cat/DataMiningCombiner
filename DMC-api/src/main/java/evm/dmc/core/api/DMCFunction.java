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

@Scope("prototype")
@Lazy
public interface DMCFunction<T> extends HasNameAndDescription {
    void execute();

    Integer getArgsCount();

    @SuppressWarnings("unchecked")
    void setArgs(Data<T>... datas);

    void setArgs(List<Data<T>> largs);

    Data<T> getResult();

    Optional<Data<T>> getOptionalResult();

    /**
     * Returns object that should be used in function object as context of
     * execution
     *
     * @return
     */
    default FrameworkContext getContext() {
        return null;
    }

    /**
     * Sets object that should be used in function object as context of
     * execution
     *
     * @return
     */
    default void setContext(FrameworkContext context) {

    }

    DMCFunction<T> setFunctionModel(FunctionModel model);

    FunctionModel getFunctionModel();

    DataModel getResultDataModel() throws DataOperationException;

    DataModel getResultDataModel(Integer previewRowsCount) throws DataOperationException;

}
