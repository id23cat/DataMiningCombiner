package evm.dmc.core.api.exceptions;

public class IndexOutOfRange extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1492670273289666593L;

	public IndexOutOfRange(Throwable exc) {
		super(exc);
	}

	public IndexOutOfRange(String message) {
		super(message);
	}

}
