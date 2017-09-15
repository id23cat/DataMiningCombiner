package evm.dmc.weka.function;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.annotations.Function;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

@Service(WekaFunctions.NORMALIZATION)
@PropertySource("classpath:weka.properties")
@Function
public class Normalization extends AbstaractWekaFilter {
	public static final String name = WekaFunctions.NORMALIZATION;
	private static FunctionType type = FunctionType.NORAMLIZATION;
	private Properties functionProperties = new Properties();

	@Value("${weka.noraml_desc}")
	String description;

	public Normalization() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	protected Filter getFilter() {
		Normalize norm = new Normalize();
		norm.setIgnoreClass(true);
		return norm;
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
		// TODO Auto-generated method stub
		
	}

}
