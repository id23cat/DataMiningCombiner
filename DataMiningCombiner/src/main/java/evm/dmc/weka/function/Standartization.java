package evm.dmc.weka.function;

import weka.filters.unsupervised.attribute.Standardize;

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
