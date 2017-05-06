package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

@Service("Weka_Normalize")
@PropertySource("classpath:weka.properties")
public class Normalization extends AbstaractWekaFilter {
	public static final String name = "Weka_Normalize";

	@Value("${weka.noraml_desc}")
	String description;

	public Normalization() {
		super();
	}

	@Override
	public void execute() {
		Filter filter = new Normalize();
		((Normalize) filter).setIgnoreClass(true);

		super.execute();

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
