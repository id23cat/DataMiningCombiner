package evm.dmc.webApi.exceptions;

/**
 * Throwed when no Framework found
 */
public class FrameworkNotFoundException extends NotFoundException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = 8935412243515578391L;

	public FrameworkNotFoundException(String message) {
		super(message);
	}

	public FrameworkNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
