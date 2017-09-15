package evm.dmc.weka.function;

import java.io.PrintStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FunctionType;
import evm.dmc.core.annotations.Function;
import evm.dmc.core.api.Data;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.exceptions.ClusteringException;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;

/**
 * Service name MUST have the same name as described in _name parameter in
 * .properites file
 * 
 * @author id23cat
 *
 */
@Service(WekaFunctions.KMEANS)
@PropertySource("classpath:weka.properties")
@Function
public class ClusteringKMeans extends AbsttractClusterer {
	public static final String name = WekaFunctions.KMEANS;
	private static FunctionType type = FunctionType.CLUSTERIZATION;
	private Properties functionProperties = new Properties();
	@WekaFW
	@Autowired
	WekaFramework fw;

	private int maxIterations = 100;
	private int numClusters = 4;
	private boolean useCanopiesReduce = true;

	@Value("${weka.kmeans_desc}")
	String description;

	// get to run 75% of CPU cores
	private int tasksToRun = (int) Math.floor(Runtime.getRuntime().availableProcessors() * 0.75);

	public ClusteringKMeans() {

	}

	// @Override
	// public void setTrainSet(Data trainSet) {
	// // SimpleKMeans skmClusterer = new SimpleKMeans(); // new instance of
	// // // clusterer
	//
	// WekaData wData = fw.castToWekaData(trainSet);
	//
	// try {
	// // skmClusterer.setMaxIterations(maxIterations);
	// // skmClusterer.setNumClusters(numClusters);
	// // skmClusterer.setNumExecutionSlots(tasksToRun);
	// //
	// skmClusterer.setReduceNumberOfDistanceCalcsViaCanopies(useCanopiesReduce);
	//
	// skmClusterer.buildClusterer(wData.getData()); // build
	// // the
	// // clusterer
	// } catch (Exception e) {
	// throw new ClusteringException(e);
	// }
	// super.clusterer = skmClusterer;
	//
	// }
	//
	// @Override
	// public void setTestSet(Data test) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public ClusteringModel getModel() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int clusterInstance(Data inst) {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	public void printInfo() {
		PrintStream out = System.out;
		Clusterer cl = super.clusterer;
		out.println(">>>>> cl.numberOfClusters():");
		try {
			out.println(cl.numberOfClusters());
		} catch (Exception e) {
			throw new ClusteringException(e);
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

//		PrintStream out = System.out;
//		out.println(">>>>>eval.clusterResultsToString()");
//		out.println(eval.clusterResultsToString());
//		out.println(">>>>>eval");
//		out.println(eval.toString());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Clusterizator train(Data trainSet) {
		WekaData wData = fw.castToWekaData(trainSet);
		try {
			SimpleKMeans skmClusterer = new SimpleKMeans();
			skmClusterer.setMaxIterations(maxIterations);
			skmClusterer.setNumClusters(numClusters);
			skmClusterer.setNumExecutionSlots(tasksToRun);
			skmClusterer.setReduceNumberOfDistanceCalcsViaCanopies(useCanopiesReduce);

			// ClusterEvaluation.crossValidateModel(skmClusterer,
			// wData.getData(), 10, new Random(42));

			skmClusterer.buildClusterer(wData.getData()); // build the clusterer

			super.clusterer = skmClusterer;
		} catch (Exception e) {
			throw new ClusteringException(e);
		}

		return this;
	}

	@Override
	protected FunctionType getFunctionType() {
		return type;
	}

	@Override
	protected Properties getProperties() {
		return functionProperties;
	}

	@Override
	protected void setFunctionProperties(Properties funProperties) {
		// TODO Auto-generated method stub
		
	}

}
