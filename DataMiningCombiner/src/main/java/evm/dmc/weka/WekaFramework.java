package evm.dmc.weka;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;

@Service
@WekaFW
public class WekaFramework extends AbstractFramework {

	@Override
	@PostConstruct
	public void initFramework() {
		super.initFrameworkForType(AbstractWekaFunction.class);
	}

}
