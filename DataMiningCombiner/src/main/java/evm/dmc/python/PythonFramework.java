package evm.dmc.python;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FrameworkType;
import evm.dmc.core.AbstractFramework;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.FrameworkContext;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;
import evm.dmc.python.data.PandasDataFrame;
import evm.dmc.python.function.AbstractPythonFunction;

@Service
@PythonFW
//@PropertySource("classpath:frameworkrepo.properties")
public class PythonFramework extends AbstractFramework {

	@Autowired
	@PythonFWContext
	FrameworkContext context;
	
//	@Value("${frameworkrepo.python_name}")
	private static final String FRAMEWORK_NAME = "pythonFramework";
	private static final FrameworkType FRAMEWORK_TYPE = FrameworkType.LOCAL;

	private static Class CLASS = AbstractPythonFunction.class;

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Framework#initFramework() Sets default abstract
	 * function type as AbstractPythonFunction.class
	 */
	@Override
	public void initFramework() {

	}

	public PythonFramework() {
	}

	@Override
	public Data getData(Class<?> type) {
		return super.instantiateData(type);
	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) throws NoSuchFunctionException {
		DMCFunction function = super.getDMCFunction(descriptor);
		function.setContext(context);
		return function;
	}

	@Override
	protected Class getFunctionClass() {
		return CLASS;
	}

	@Override
	public Data castToNativeData(Data data) throws ClassCastException {
		// TODO
		// return castToPyVar(data);
		return null;
	}

	public PandasDataFrame castToPandasData(Data data) {
		PandasDataFrame pdsData = data instanceof PandasDataFrame ? (PandasDataFrame) data : null;
		if (pdsData == null) {
			throw new ClassCastException("Unsupported data type");
		}
		return pdsData;
	}

	@Override
	protected String getFrameworkName() {
		return FRAMEWORK_NAME;
	}

	@Override
	protected FrameworkType getFrameworkType() {
		return FRAMEWORK_TYPE;
	}

	// public PyVar castToPyVar(Data data) {
	// PyVar pyVar = data instanceof PyVar ? (PyVar) data : null;
	// if (pyVar == null) {
	// throw new ClassCastException("Unsupported data type");
	// }
	// return pyVar;
	// }

}
