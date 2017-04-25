package evm.dmc.weka.function;

import org.springframework.stereotype.Service;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

@Service("Meka_Normalize")
public class Normalization extends AbstaractWekaFilter {

	public Normalization() {
		super();
		super.setName("Meka Normalize");
	}

	@Override
	public void execute() {
		Filter filter = new Normalize();
		((Normalize) filter).setIgnoreClass(true);

		super.execute();

	}

}
