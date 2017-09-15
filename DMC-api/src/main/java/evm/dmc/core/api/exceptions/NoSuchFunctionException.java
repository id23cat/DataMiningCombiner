package evm.dmc.core.api.exceptions;

public class NoSuchFunctionException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7373572140135789472L;

	public NoSuchFunctionException(Throwable exc) {
		super(exc);
	}

	public NoSuchFunctionException(String message) {
		super(message);
	}
	
	public NoSuchFunctionException(String message, Throwable exc) {
		super(message, exc);
	}

}
