package evm.dmc.weka.function;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.Function;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.CSVSaver;
import evm.dmc.core.api.exceptions.StoreDataException;
import evm.dmc.core.exceptions.UncheckedStoringException;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.WekaFunction;
import weka.core.Instances;
import weka.core.converters.AbstractSaver;

@Service(WekaFunctions.CSVSAVER)
@PropertySource("classpath:weka.properties")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Function
public class WekaCSVSave extends AbstractDMCFunction<String> implements CSVSaver, WekaFunction  {
	private static final String NAME = WekaFunctions.CSVSAVER;
	private static final Integer ARGS_COUNT = 1;
	private static final String DEST_PARAM = "destination";
	private static FunctionType type = FunctionType.CSV_DATADESTINATION;
	private Properties functionProperties = new Properties();

	private File destination = null;
	private Data<String> save = null;

	@Autowired
	WekaFramework framework;

	@Value("${weka.csvsave_desc}")
	String description;
	
	public WekaCSVSave() {
		functionProperties.setProperty(DEST_PARAM, "");
	}

	@Override
	public void save(Data data) throws ClassCastException, StoreDataException {
		save = data;
		try {
			execute();
		} catch (UncheckedStoringException e) {
			throw new StoreDataException("Trying to save data to csv failed", e);
		}
	}

	@Override
	public CSVSaver setDestination(String filename) {
		destination = new File(filename);
		return this;
	}

	@Override
	public CSVSaver setDestination(File file) {
		this.destination = file;
		return this;
	}

	@Override
	public void execute() throws ClassCastException, UncheckedStoringException {

		AbstractSaver saver = new weka.core.converters.CSVSaver();
		if (destination == null)
			throw new UncheckedStoringException("Destination file does not declred");
		try {
			saver.setFile(destination);
			saver.setInstances(framework.castToWekaData(save).getData());
			saver.writeBatch();
		} catch (IOException e) {
			throw new UncheckedStoringException(e);
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Integer getArgsCount() {
		return ARGS_COUNT;
	}

	@Override
	public void setArgs(Data<String>... datas) {
		setDestination(datas[0].getData());

	}

	@Override
	public void setArgs(List<Data<String>> largs) {
		setDestination(largs.get(0).getData());

	}

	/*
	 * Returns destination file name
	 * (non-Javadoc)
	 * 
	 * 
	 * @see evm.dmc.core.function.DMCFunction#getResult()
	 */
	@Override
	public Data<String> getResult() {
		@SuppressWarnings("unchecked")
		Data<String> data = framework.getData(String.class);
		data.setData(destination.getAbsolutePath());
		return data;
	}

	@Override
	protected FunctionType getFunctionType() {
		return type;
	}

	@Override
	protected Properties getProperties() {
		return functionProperties;
	}

	@Override
	protected void setFunctionProperties(Properties funProperties) {
		setDestination(funProperties.getProperty(DEST_PARAM));
		
	}
	
	@Override
	public Optional<Data<String>> getOptionalResult() {
		return Optional.ofNullable(getResult());
	}

}
