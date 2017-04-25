package evm.dmc.python;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import evm.dmc.core.DataConverter;
import evm.dmc.core.FrameworkContext;
import evm.dmc.core.InContextExecutable;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.StringData;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.core.function.DMCFunction;
import jep.Jep;
import jep.JepException;

@Controller
public abstract class AbstractPythonFunction extends AbstractDMCFunction<String>
		implements DataConverter, InContextExecutable {

	@Service
	@PythonFWContext
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
				execScript(resetScript);
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
			// concatenate construntion from parts:
			// <resutVar> = <functionName>(<args>...):

			// set the <resultVar>
			StringBuilder command = new StringBuilder((String) func.getResult().getData());
			command.append("=");
			// set <functionName>
			command.append(func.function);
			command.append("(");
			for (Data<String> arg : func.arguments) {
				command.append(arg.getData());
				command.append(",");
			}
			command.insert(command.lastIndexOf(","), ")");
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
				func.getResult().setData(result.toString());
			} catch (JepException e) {
				throw new RuntimeException("Pulling data from Jep has failed", e);
			}

		}

		private void execFunction(String command) throws JepException {
			jep.eval(command);
		}

		private void execScript(String script) throws JepException {
			jep.runScript(scriptsFilder + script);
		}
	}

	@Autowired
	@PythonFWContext
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
	public Data getResult() {
		if (this.result == null)
			throw new NullPointerException("The result is undefined");
		return this.result;
	}

	public void setResult(Data result) {
		this.result = result;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public StringData convert(Data data) {
		super.setArgs(data);
		setResult(fw.getData(StringData.class));
		context.getValue(this);
		return (StringData) result;

	}

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

}
