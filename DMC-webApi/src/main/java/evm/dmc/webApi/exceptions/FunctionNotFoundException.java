package evm.dmc.webApi.exceptions;

/**
 * Throwed when no Function found
 */
public class FunctionNotFoundException extends NotFoundException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -2662782984482995244L;

	public FunctionNotFoundException(String message){
		super(message);
	}

	public FunctionNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
