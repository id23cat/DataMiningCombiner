package evm.dmc.weka.exceptions;

public class ClusteringException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6589657450650679421L;

	public ClusteringException(Throwable cause) {
		super(cause);
	}

	public ClusteringException(String string) {
		super(string);
	}
}
