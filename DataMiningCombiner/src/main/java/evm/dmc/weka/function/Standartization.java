package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import weka.filters.unsupervised.attribute.Standardize;

@Service("Weka_Standartize")
@PropertySource("classpath:weka.properties")
public class Standartization extends AbstaractWekaFilter {
	public static final String name = "Weka_Standartize";

	@Value("${weka.standart_desc}")
	String description;

	public Standartization() {
		super();
	}

	@Override
	public void execute() {
		super.filter = new Standardize();
		((Standardize) filter).setIgnoreClass(true);

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
