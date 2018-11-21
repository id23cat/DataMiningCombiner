package evm.dmc.webApi.exceptions;

/**
 * Throwed when error occurs with data access
 */
public class StorageException extends RuntimeException {

    /** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -3268906588922725839L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
