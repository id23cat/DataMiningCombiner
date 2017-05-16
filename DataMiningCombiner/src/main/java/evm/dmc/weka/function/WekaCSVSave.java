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

import evm.dmc.core.data.Data;
import evm.dmc.core.exceptions.StoringException;
import evm.dmc.core.exceptions.UncheckedStoringException;
import evm.dmc.core.function.CSVSaver;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.weka.WekaFramework;
import weka.core.converters.AbstractSaver;

@Service(WekaFunctions.CSVSAVER)
@PropertySource("classpath:weka.properties")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WekaCSVSave implements CSVSaver, DMCFunction<String> {
	private static final String NAME = WekaFunctions.CSVSAVER;
	private static final Integer ARGS_COUNT = 1;

	private File destination = null;
	private Data save = null;

	@Autowired
	WekaFramework framework;

	@Value("${weka.csvsave_desc}")
	String description;

	@Override
	public void save(Data data) throws ClassCastException, StoringException {
		save = data;
		try {
			execute();
		} catch (UncheckedStoringException e) {
			throw new StoringException("Trying to save data to csv failed", e);
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

}
