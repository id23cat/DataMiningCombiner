package evm.dmc.core.function;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.PostConstruct;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionType;
import evm.dmc.core.annotations.Function;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;

/**
 * @author id23cat
 *
 */
public abstract class AbstractDMCFunction<T> implements DMCFunction<T> {

	/**
	 * Name of the function, is used as key in map of functions presented in
	 * Framework collection
	 */
	// protected String description;
	/**
	 * Count of the parameters have to be setted as arguments
	 */
	public static final String EMPTY_RESULT_EXC_MSG = "Empy result field: probably execution was not called";
	protected Integer argsCount;

	protected List<Data<T>> arguments;

	protected FunctionModel functionModel = new FunctionModel();

	public AbstractDMCFunction() {
		setArgsCount(getArgsCount());
		// this.description = funcName();

	}

	// TODO: replace with static functions to avoid creating exemplar for
	// getting FunctionModel
	protected abstract FunctionType getFunctionType();

	protected abstract Properties getProperties();

	protected abstract void setFunctionProperties(Properties funProperties);

	@PostConstruct
	protected void initFunction() {
		functionModel.setName(getName());
		functionModel.setType(getFunctionType());
		functionModel.setProperties(FunctionModel.propertiesToMap(getProperties()));
		functionModel.setDescription(getDescription());
	}

	/**
	 * Each final implementation of AbstractDMCFunction must set count of
	 * accepted arguments
	 * 
	 * @param paramCount
	 *            the paramCount to set
	 */
	protected void setArgsCount(Integer paramCount) {
		this.argsCount = paramCount;
	}

	public void setArgs(Data<T>... datas) {
		this.arguments = Arrays.asList(datas).subList(0, this.argsCount);
	}

	@Override
	public void setArgs(List<Data<T>> args) {
		this.arguments = args.subList(0, this.argsCount);
	}

	/**
	 * @return the arguments
	 */
	public List<Data<T>> getArguments() {
		return arguments;
	}

	public DMCFunction<T> setFunctionModel(FunctionModel model) {
		this.functionModel = model;
		setFunctionProperties(FunctionModel.mapToProperties(this.functionModel.getProperties()));
		return this;
	}

	public FunctionModel getFunctionModel() {
		return this.functionModel;
	}

	public DataModel getResultDataModel() throws DataOperationException {
		Optional<Data<T>> result = getOptionalResult();
		return result.map(Data<T>::getDataModel).orElseThrow(() -> new DataOperationException(EMPTY_RESULT_EXC_MSG));
		// return getResult().getDataModel();
	}

	public DataModel getResultDataModel(Integer previewRowsCount) throws DataOperationException {
		Optional<Data<T>> result = getOptionalResult();
		return result.map((Data<T> data) -> data.getDataModel(previewRowsCount))
				.orElseThrow(() -> new DataOperationException(EMPTY_RESULT_EXC_MSG));
		// return getResult().getDataModel(previewRowsCount);
	}

	/**
	 * Checks that all important properties filled correctly
	 *
	 * @return true, if successful
	 */
	protected boolean check() {

		if (argsCount == null || argsCount < 0)
			return false;
		if (arguments == null || arguments.size() != argsCount)
			return false;
		return true;
	}

}
