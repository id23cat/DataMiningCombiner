package evm.dmc.weka.function;

import weka.core.Instances;
import weka.filters.Filter;

public class AbstaractWekaFilter extends AbstractWekaFunction {
	protected Filter filter;

	AbstaractWekaFilter() {
		super();
		super.setArgsCount(1);
	}

	@Override
	public void execute() {
		Instances inst = super.arguments.get(0).getData();
		try {
			filter.setInputFormat(inst);
			inst = Filter.useFilter(inst, filter);
		} catch (Exception e) {
			throw new FilterError(e);
		}
		super.arguments.get(0).setData(inst);
		super.result = super.arguments.get(0);

	}

}
