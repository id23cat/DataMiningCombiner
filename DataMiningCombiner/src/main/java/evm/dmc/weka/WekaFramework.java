package evm.dmc.weka;

import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.weka.function.AbstractWekaFunction;

@Service
@WekaFW
public class WekaFramework extends AbstractFramework {

	public WekaFramework() {
		super(AbstractWekaFunction.class);
	}

	@Override
	public void initFramework() {
		super.initFrameworkForType(AbstractWekaFunction.class);
	}

}
