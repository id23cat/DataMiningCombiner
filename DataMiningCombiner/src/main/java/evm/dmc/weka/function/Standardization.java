package evm.dmc.weka.function;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.annotations.Function;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

@Service(WekaFunctions.STANDARDIZATION)
@PropertySource("classpath:weka.properties")
@Function
public class Standardization extends AbstaractWekaFilter {
	public static final String name = WekaFunctions.STANDARDIZATION;
	private static final FunctionType TYPE = FunctionType.STANDARDIZATION;
	private Properties functionProperties = new Properties();
	
	@Value("${weka.standart_desc}")
	String description;

	public Standardization() {
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
		Standardize std = new Standardize();
		std.setIgnoreClass(true);
		return std;
	}

	@Override
	protected FunctionType getFunctionType() {
		return TYPE;
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
