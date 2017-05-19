package evm.dmc.weka.function;

import evm.dmc.core.data.Data;

public interface Clusterizator {
	// void setTrainSet(Data trainSet);
	//
	// void setTestSet(Data testSet);
	//
	// ClusteringModel getModel();
	//
	// int clusterInstance(Data inst);

	Clusterizator train(Data trainSet);

}
