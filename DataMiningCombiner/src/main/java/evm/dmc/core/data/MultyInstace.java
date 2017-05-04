package evm.dmc.core.data;

public interface MultyInstace {
	int getInstancesCount();

	Data<?> getSubset(int from, int to);

	/**
	 * @param foldNum
	 *            number of fold in [0, 3]
	 * @return train set (75%) and test set (25%) for cross-validation
	 */
	Data<?>[] getTrainTest(int foldNum);

}
