package evm.dmc.core.data;

import evm.dmc.core.api.Data;

public interface HasMultiInstaces {
	int getInstancesCount();

	Data<?> getInstance(int index);

	Data<?> getSubset(int from, int to);

	/**
	 * @param foldNum
	 *            number of fold in [0, 3]
	 * @return train set (75%) and test set (25%) for cross-validation
	 */
	Data<?>[] getTrainTest(int foldNum);

}
