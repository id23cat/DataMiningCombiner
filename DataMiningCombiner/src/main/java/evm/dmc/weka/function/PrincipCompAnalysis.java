package evm.dmc.weka.function;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.Function;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

@Service(WekaFunctions.PCA)
@PropertySource("classpath:weka.properties")
@Function
public class PrincipCompAnalysis extends AbstaractWekaFilter {
	private final static String NAME = WekaFunctions.PCA;
	private static FunctionType type = FunctionType.PCA;
	private Properties functionProperties = new Properties();

	// TODO: use "Attribute" instead of hardcoded:
	private int components = 2;

	@Value("${weka.pca_desc}")
	private String description;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	protected Filter getFilter() {
		PrincipalComponents pca = new PrincipalComponents();
		pca.setMaximumAttributes(components);
		return pca;
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
