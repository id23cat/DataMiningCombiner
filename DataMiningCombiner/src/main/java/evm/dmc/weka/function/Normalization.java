package evm.dmc.weka.function;

import org.springframework.stereotype.Service;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

@Service("Weka_Normalize")
public class Normalization extends AbstaractWekaFilter {

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
		return "Weka_Normalize";
	}

	@Override
	public String getDescription() {
		return "Executes normalization of dataset";
	}

}
