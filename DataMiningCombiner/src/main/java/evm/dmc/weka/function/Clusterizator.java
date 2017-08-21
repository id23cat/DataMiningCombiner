package evm.dmc.weka.function;

import evm.dmc.core.api.Data;

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
