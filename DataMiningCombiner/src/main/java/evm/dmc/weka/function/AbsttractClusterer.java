package evm.dmc.weka.function;

import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.exceptions.ClusteringException;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * Applies one argument:
 * <li>set to which need to apply trained clustering model
 * 
 * 
 * @author id23cat
 *
 */
public abstract class AbsttractClusterer extends AbstractWekaFunction implements Clusterizator {
	protected final static Integer argCount = 1;

	protected Clusterer clusterer;
	protected ClusterEvaluation eval = new ClusterEvaluation();

	// @WekaFW
	// @Autowired
	// WekaFramework fw;

	// details at
	// https://weka.wikispaces.com/Use+Weka+in+your+Java+code#Clustering
	@Override
	public void execute() throws ClusteringException {
		eval.setClusterer(clusterer);
		try {
			eval.evaluateClusterer(super.arguments.get(0).getData());
		} catch (Exception e) {
			throw new ClusteringException(e);
		}

		WekaData res = (WekaData) ((WekaData) super.arguments.get(0)).clone();
		Instances instRes = res.getData();

		// Add new attribute with cluster number at the end
		Attribute attr = new Attribute("Cluster");

		System.out.println("NumArrts before " + instRes.numAttributes());
		int attrIndex = instRes.numAttributes();
		instRes.insertAttributeAt(attr, attrIndex);
		System.out.println("NumArrts after " + instRes.numAttributes());

		// Insert all cluster assignments to created attribute
		double[] clustAssigs = eval.getClusterAssignments();
		for (int i = 0; i < clustAssigs.length; i++) {
			instRes.instance(i).setValue(attrIndex, clustAssigs[i]);
		}

		super.setResult(res);

	}

	@Override
	public Integer getArgsCount() {
		return argCount;
	}

}
