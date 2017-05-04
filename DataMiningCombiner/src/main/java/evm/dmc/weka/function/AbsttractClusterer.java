package evm.dmc.weka.function;

import weka.clusterers.Clusterer;

/**
 * Applies one argument:
 * <li>set to which need to apply trained clustering model
 * 
 * @author id23cat
 *
 */
public abstract class AbsttractClusterer extends AbstractWekaFunction implements Clusterizator {
	final static Integer argCount = 2;

	protected Clusterer clusterer;

	@Override
	public void execute() {

	}

	@Override
	public Integer getArgsCount() {
		return argCount;
	}

}
