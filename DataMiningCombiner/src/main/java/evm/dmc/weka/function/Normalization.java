package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

@Service(WekaFunctions.NORMALIZATION)
@PropertySource("classpath:weka.properties")
public class Normalization extends AbstaractWekaFilter {
	public static final String name = WekaFunctions.NORMALIZATION;

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

}
