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

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.api.model.FunctionType;
import evm.dmc.core.DataFactory;
import evm.dmc.core.annotations.Function;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.back.CSVLoader;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.core.api.exceptions.LoadDataException;
import evm.dmc.core.function.AbstractDMCFunction;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.WekaFunction;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.exceptions.LoadHeaderException;
import weka.core.Instances;

@Service(WekaFunctions.CSVLOADER)
@PropertySource("classpath:weka.properties")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Function
public class WekaCSVLoad extends AbstractDMCFunction<Instances>
		implements CSVLoader, WekaFunction {
	private static final String NAME = WekaFunctions.CSVLOADER;
	private static final Integer ARGS_COUNT = 0;
	private static final String SOURCE_PARAM = "source";
	private static final String TYPE_PARAM = "srcType";
	private static FunctionType type = FunctionType.CSV_DATASOURCE;
	private Properties functionProperties = new Properties();
	
	private FunctionSrcModel model = FunctionSrcModel.srcBuilder().build();

	private DataFactory dataFactory;

//	private String source = null;
	private Data<Instances> result = null;

	private StringBuilder dateAttributes = new StringBuilder();
	private StringBuilder numericAttributes = new StringBuilder();
	private StringBuilder nominalAttributes = new StringBuilder();
	private StringBuilder stringAttributes = new StringBuilder();

	private boolean hasHeader = true;

	@Value("${weka.csvload_desc}")
	private String description;

	private weka.core.converters.CSVLoader loader = new weka.core.converters.CSVLoader();

	public WekaCSVLoad(@Autowired @WekaFW DataFactory dataFactory) {
		super();
		this.dataFactory = dataFactory;
		functionProperties.setProperty(SOURCE_PARAM, "");
		super.functionModel = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.function.DMCDataLoader#get()
	 *
	 */
	@Override
	public Data<Instances> get() throws LoadDataException {
		if (result == null)
			this.execute();
		return result;
	}

	@Override
	public void restart() throws LoadDataException {
		result = null;
		try {
			loader.reset();
		} catch (IOException e) {

			throw new LoadDataException(e);
		}
	}

	@Override
	public CSVLoader setSource(String source) {
		if (!model.getSource().equals(source)) { // argument is not equal actual
												// source
			result = null;
		}
		model.setSource(source);
		model.setTypeSrcDst(DataSrcDstType.LOCAL_FS);
		return this;
	}

	// public String getSourceDescription() throws LoadDataException {
	// try {
	// weka.core.converters.CSVLoader loader = new
	// weka.core.converters.CSVLoader();
	// loader.setNoHeaderRowPresent(!hasHeader);
	// loader.setSource(new File(this.source));
	// // loader.getNextInstance(structure)
	// return loader.getFileDescription();
	// } catch (Throwable e) {
	// throw checkException(e);
	// }
	// }

	/**
	 * Returns header (if present in file otherwise generated) and first line of
	 * data
	 * 
	 * @return String contains header(with recognized types and first line of
	 *         data
	 * @throws LoadDataException
	 */
	public String getHead() throws LoadDataException {
		try {
			// weka.core.converters.CSVLoader loader = new
			// weka.core.converters.CSVLoader();
			setAttributes();

			Instances inst = loader.getStructure();
			inst.add(loader.getNextInstance(inst));

			WekaData tmp = (WekaData) dataFactory.getData(WekaData.class);
			tmp.setData(inst);

			return tmp.getAllAsString();
		} catch (Throwable e) {
			throw checkException(e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws LoadDataException {
		Instances inst = null;

		try {
			// weka.core.converters.CSVLoader loader = new
			// weka.core.converters.CSVLoader();

			this.setAttributes();

			inst = loader.getDataSet();
		} catch (Throwable e) {
			throw checkException(e);
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
		return description;
	}

	@Override
	public Integer getArgsCount() {
		return ARGS_COUNT;
	}

	@Override
	public Data<Instances> getResult() {
		return this.get();
	}

	@Override
	public CSVLoader hasHeader(boolean b) {
		this.hasHeader = b;
		loader.setNoHeaderRowPresent(!hasHeader);
		return this;
	}

	public boolean getHasHeader() {
		return hasHeader;
	}

	@Override
	public CSVLoader asDate(int index) {
		dateAttributes.append(index + 1 + ",");
		return this;
	}

	@Override
	public CSVLoader setDateFormat(String format) {
		loader.setDateFormat(format);
		return this;
	}

	@Override
	public CSVLoader asNumeric(int index) {
		numericAttributes.append(index + 1 + ",");
		return this;
	}

	@Override
	public CSVLoader asNominal(int index) {
		nominalAttributes.append(index + 1 + ",");
		return this;
	}

	@Override
	public CSVLoader asString(int index) {
		stringAttributes.append(index + 1 + ",");
		return this;
	}

	private LoadDataException checkException(Throwable e) {
		// probably csv file hasn't header
		if (e instanceof IllegalArgumentException && e.getMessage().startsWith("Attribute names are not unique!")) {
			return new LoadHeaderException("Probably file has no header: " + e.getMessage(), e);
		} else
			return new LoadDataException(e);
	}

	private void setAttributes() throws IOException {
		loader.setDateAttributes(dateAttributes.toString());
		loader.setNominalAttributes(nominalAttributes.toString());
		loader.setNumericAttributes(numericAttributes.toString());
		loader.setStringAttributes(stringAttributes.toString());
		loader.setSource(new File(model.getSource()));
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
			setSource(funProperties.getProperty(SOURCE_PARAM));
	}

	@Override
	public Optional<Data<Instances>> getOptionalResult() {
		return Optional.ofNullable(getResult());
	}

	@Override
	public FunctionSrcModel getSrcModel() {
		return model;
	}

	@Override
	public DMCDataLoader setSrcModel(FunctionSrcModel model) {
		this.model = model;
		super.functionModel = this.model;
		setFunctionProperties(FunctionModel.mapToProperties(this.model.getProperties()));
		
		return this;
	}
	
	@Override
	public WekaCSVLoad setFunctionModel(FunctionModel model) {
		this.model = new FunctionSrcModel(model);
		setFunctionProperties(FunctionModel.mapToProperties(this.model.getProperties()));
		return this;
	}

	@Override
	public FunctionModel getFunctionModel() {
		return this.model;
	}

}
