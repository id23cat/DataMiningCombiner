package evm.dmc.webApi.exceptions;

/**
 * Throwed when no Algorithm found
 */
public class AlgorithmNotFoundException extends NotFoundException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = 6065449387879839019L;

	public AlgorithmNotFoundException(String message){
		super(message);
	}

	public AlgorithmNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
