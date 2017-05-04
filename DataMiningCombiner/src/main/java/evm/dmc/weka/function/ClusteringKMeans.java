package evm.dmc.weka.function;

import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.core.data.Data;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.ClusteringModel;
import evm.dmc.weka.data.WekaData;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;

@Service("Weka_KMeansClustering")
public class ClusteringKMeans extends AbsttractClusterer {
	@WekaFW
	@Autowired
	WekaFramework fw;

	private int maxIterations = 100;
	private int numClusters = 4;
	private boolean useCanopiesReduce = true;

	// get to run 75% of CPU cores
	private int tasksToRun = (int) Math.floor(Runtime.getRuntime().availableProcessors() * 0.75);

	@Override
	public void setTrainSet(Data trainSet) {
		SimpleKMeans skmClusterer = new SimpleKMeans(); // new instance of
														// clusterer

		WekaData wData = fw.castToWekaData(trainSet);

		try {
			skmClusterer.setMaxIterations(maxIterations);
			skmClusterer.setNumClusters(numClusters);
			skmClusterer.setNumExecutionSlots(tasksToRun);
			skmClusterer.setReduceNumberOfDistanceCalcsViaCanopies(useCanopiesReduce);

			skmClusterer.buildClusterer(wData.getData()); // build
															// the
															// clusterer
		} catch (Exception e) {
			throw new ClusteringError(e);
		}
		super.clusterer = skmClusterer;

	}

	@Override
	public void setTestSet(Data test) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClusteringModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int clusterInstance(Data inst) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printInfo() {
		PrintStream out = System.out;
		Clusterer cl = super.clusterer;
		out.println(">>>>> cl.numberOfClusters():");
		try {
			out.println(cl.numberOfClusters());
		} catch (Exception e) {
			throw new ClusteringError(e);
		}
		out.println(">>>>> cl.toString():");
		out.println(cl.toString());
		// out.println(cl.distributionForInstance(arg0)
		out.println(">>>>> cl.getCapabilities():");
		out.println(cl.getCapabilities().toString());

		SimpleKMeans kms = (SimpleKMeans) cl;
		out.println(">>>>> kms.distanceFunctionTipText():");
		out.println(kms.distanceFunctionTipText());
		out.println(">>>>> kms.getMaxIterations():");
		out.println(kms.getMaxIterations());
		out.println(">>>>> kms.globalInfo():");
		out.println(kms.globalInfo());

	}

	public void evaluate(Data newData) {
		WekaData wData = fw.castToWekaData(newData);
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(clusterer); // the cluster to evaluate
		try {
			eval.evaluateClusterer(wData.getData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintStream out = System.out;
		out.println(">>>>>eval.clusterResultsToString()");
		out.println(eval.clusterResultsToString());
		out.println(">>>>>eval");
		out.println(eval.toString());
	}

}
