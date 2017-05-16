package evm.dmc.weka.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import weka.filters.unsupervised.attribute.Standardize;

@Service(WekaFunctions.STANDARDIZATION)
@PropertySource("classpath:weka.properties")
public class Standartization extends AbstaractWekaFilter {
	public static final String name = WekaFunctions.STANDARDIZATION;

	@Value("${weka.standart_desc}")
	String description;

	public Standartization() {
		super();
	}

	@Override
	public void execute() {
		super.filter = new Standardize();
		((Standardize) super.filter).setIgnoreClass(true);

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
