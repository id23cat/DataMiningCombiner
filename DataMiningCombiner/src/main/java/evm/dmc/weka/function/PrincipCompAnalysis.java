package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

@Service(WekaFunctions.PCA)
@PropertySource("classpath:weka.properties")
public class PrincipCompAnalysis extends AbstaractWekaFilter {
	private final static String NAME = WekaFunctions.PCA;

	// TODO: use "Attribute" instead of hardcoded:
	private int components = 2;

	@Value("${weka.pca_desc}")
	private static String description;

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

}
