package evm.dmc.weka.function;

import org.springframework.stereotype.Service;

import weka.filters.unsupervised.attribute.Standardize;

@Service("Weka_Standartize")
public class Standartization extends AbstaractWekaFilter {

	public Standartization() {
		super();
		super.setName("Meka Standartize");
	}

	@Override
	public void execute() {
		super.filter = new Standardize();
		((Standardize) filter).setIgnoreClass(true);

		super.execute();

	}

}
