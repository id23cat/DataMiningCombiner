package evm.dmc.weka;

import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.core.data.Data;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.function.AbstractWekaFunction;
import evm.dmc.weka.function.ClusteringError;
import weka.core.Instances;

@Service
@WekaFW
public class WekaFramework extends AbstractFramework {
	static Class CLASS = AbstractWekaFunction.class;

	public WekaFramework() {
		// super(CLASS);
	}

	@Override
	public void initFramework() {
		super.initFrameworkForType(CLASS);
	}

	public WekaData castToWekaData(Data data) throws ClusteringError {
		WekaData wekaData = data instanceof WekaData ? (WekaData) data : null;
		if (wekaData == null) {
			Instances inst = data.getData() instanceof Instances ? (Instances) data.getData() : null;
			if (inst == null)
				throw new ClusteringError("Unsupported data type");
			wekaData = (WekaData) super.getData(WekaData.class);
			wekaData.setData(inst);
		}
		return wekaData;
	}

	protected Class getFunctionClass() {
		return CLASS;
	}

}
