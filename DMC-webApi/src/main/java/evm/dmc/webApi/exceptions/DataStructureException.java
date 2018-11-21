package evm.dmc.webApi.exceptions;

/**
 * Throwed when error occurs with data structures
 */
public class DataStructureException extends StorageException {

    /** Defined to avoid problems with serialization */
	private static final long serialVersionUID = 8062739962560746393L;

	public DataStructureException(String message) {
        super(message);
    }

    public DataStructureException(String message, Throwable cause) {
        super(message, cause);
    }

}
