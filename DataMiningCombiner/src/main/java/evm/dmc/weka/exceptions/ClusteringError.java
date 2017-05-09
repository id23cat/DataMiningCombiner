package evm.dmc.weka.exceptions;

public class ClusteringError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6589657450650679421L;

	public ClusteringError(Throwable cause) {
		super(cause);
	}

	public ClusteringError(String string) {
		super(string);
	}
}
