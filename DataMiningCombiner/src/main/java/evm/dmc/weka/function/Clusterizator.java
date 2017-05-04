package evm.dmc.weka.function;

import evm.dmc.core.data.Data;
import evm.dmc.weka.data.ClusteringModel;

public interface Clusterizator {
	void setTrainSet(Data trainSet);

	void setTestSet(Data testSet);

	ClusteringModel getModel();

	int clusterInstance(Data inst);

}
