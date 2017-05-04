package evm.dmc.weka.function;

public class ClusteringError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6589657450650679421L;

	ClusteringError(Throwable cause) {
		super(cause);
	}

	public ClusteringError(String string) {
		super(string);
	}
}
