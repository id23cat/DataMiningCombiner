package evm.dmc.python.function;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import evm.dmc.core.DataConverter;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.FrameworkContext;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.python.PythonFWContext;
import evm.dmc.python.PythonFramework;
import jep.Jep;
import jep.JepException;

@Controller
public abstract class AbstractPythonFunction extends AbstractDMCFunction<String> implements DataConverter {

	@Service
	@PythonFWContext
	@DependsOn("pythonFramework")
	public static class JepPythonContext implements FrameworkContext {
		@Autowired
		private Jep jep;

		@Value("${jep.scriptsFolder}")
		private String scriptsFilder;
		@Value("${jep.initScript}")
		private String initScript;
		@Value("${jep.resetScript}")
		private String resetScript;

		@Override
		@PostConstruct
		public void initContext() {

			try {
				execScript(initScript);
			} catch (JepException e) {
				throw new RuntimeException("Initialization of Jep has failed", e);
			}
		}

		@PreDestroy
		public void destroy() {
			jep.close();
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void executeInContext(DMCFunction function) {
			AbstractPythonFunction func = (AbstractPythonFunction) function;

			StringBuilder command = new StringBuilder((String) func.result.getData());
			command.append("=");
			// set <functionName>
			command.append(func.function);
			command.append("(");
			for (Data<String> arg : func.arguments) {
				command.append(arg.getData());
				command.append(",");
			}
			int idx = command.lastIndexOf(",");
			command.replace(idx, idx + 1, ")");
			System.out.println(command.toString());
			try {
				execFunction(command.toString());
			} catch (JepException e) {
				throw new RuntimeException("Execeution in Jep has failed", e);
			}

		}

		@Override
		public void getValue(DMCFunction function) {
			AbstractPythonFunction func = (AbstractPythonFunction) function;
			try {
				Object result = jep.getValue(func.arguments.get(0).getData());
				func.result.setData(result.toString());
			} catch (JepException e) {
				throw new RuntimeException("Pulling data from Jep has failed", e);
			}

		}

		public String getCurENV() {
			String res;
			try {
				execFunction("env = dir()");
				res = jep.getValue("env").toString();
			} catch (JepException e) {
				throw new RuntimeException("Getting environment from Jep has failed", e);
			}

			return res;
		}

		private void execFunction(String command) throws JepException {
			jep.eval(command);
		}

		private void execScript(String script) throws JepException {
			jep.runScript(scriptsFilder + script);
		}
	}

	private FrameworkContext context;

	@Autowired
	PythonFramework fw;

	private String function = null;

	private Data result = null;

	@Override
	public FrameworkContext getContext() {
		return context;
	}

	@Override
	public void setContext(FrameworkContext context) {
		this.context = context;
	}

	@Override
	public void execute() {
		if (!check())
			throw new NullPointerException("The operation is undefined");
		context.executeInContext(this);
	}

	/**
	 * @return the result of operation
	 */
	@Override
	public Data getResult() throws NullPointerException {
		if (this.result == null || !getCurrentEnvironment().contains(this.result.getData().toString())) {
			StringBuilder strb = new StringBuilder("The result is undefined (variable \'");
			strb.append(this.result.getData().toString());
			strb.append("\'). Perhaps reason: execute() method does not called");
			throw new NullPointerException(strb.toString());
		}
		return this.result;
	}

	public void setResult(Data result) {
		this.result = result;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	// @Override
	// public StringData convert(Data data) {
	// super.setArgs(data);
	// setResult(fw.getData(StringData.class));
	// context.getValue(this);
	// return (StringData) result;
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.function.AbstractDMCFunction#check()
	 */
	@Override
	protected boolean check() {
		if (context == null)
			throw new NullPointerException("FrameworkContext not defined");
		if (function == null)
			throw new NullPointerException("Function not defined");

		return super.check();
	}

	String getCurrentEnvironment() {
		return ((JepPythonContext) context).getCurENV();
	}

}
