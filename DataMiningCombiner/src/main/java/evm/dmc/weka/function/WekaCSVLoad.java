package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

import evm.dmc.core.DataFactory;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.exceptions.LoadDataError;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Service("Weka_CSVLoader")
@PropertySource("classpath:weka.properties")
public class WekaCSVLoad implements CSVLoader, DMCFunction<Instances> {
	private static final String NAME = "Weka_CSVLoader";
	private static final Integer ARGS_COUNT = 0;
	private DataFactory dataFactory;

	private String source;
	private Data result = null;

	@Value("${weka.csvload_desc}")
	String description;

	public WekaCSVLoad(@Autowired @WekaFW DataFactory dataFactory) {
		super();
		this.dataFactory = dataFactory;
	}

	@Override
	public Data get() throws LoadDataError {
		if (result == null)
			execute();
		return result;
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		Instances inst = null;
		try {
			inst = DataSource.read(this.source);
		} catch (Exception e) {
			throw new LoadDataError(e);
		}
		result = dataFactory.getData(WekaData.class);
		result.setData(inst);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getArgsCount() {
		return ARGS_COUNT;
	}

	@Override
	public void setArgs(Data<Instances>... datas) {
	}

	@Override
	public void setArgs(List<Data<Instances>> largs) {
	}

	@Override
	public Data getResult() {
		return this.get();
	}

}
