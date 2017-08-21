package evm.dmc.core;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;

public abstract class AbstractFunctionFactory implements DMCFunctionFactory {
	protected Map<String, Framework> functionsMap = new HashMap<>();
	@Autowired
	protected Set<Framework> frameworks;

	public AbstractFunctionFactory() {
	};

	public void setFrameworks(Set<Framework> fwSet) {
		this.frameworks = fwSet;
	}

	@Override
	public DMCFunction getFunction(String descriptor) {
		return functionsMap.get(descriptor).getDMCFunction(descriptor);

	}

	@Override
	public void addFramework(Framework framework) {
		this.frameworks.add(framework);
	}

	@Override
	public void initFactory() {
		buildFunctionsMap();
	}

	protected void buildFunctionsMap() {
		for (Framework fwk : frameworks) {
			Set<String> functions = fwk.getFunctionDescriptors();
			for (String descr : functions) {
				functionsMap.put(descr, fwk);
			}
		}
	}

}
