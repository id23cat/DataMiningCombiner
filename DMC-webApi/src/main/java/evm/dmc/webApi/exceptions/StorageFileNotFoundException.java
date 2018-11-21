package evm.dmc.webApi.exceptions;

/**
 * Throwed when no file found in storage
 */
public class StorageFileNotFoundException extends StorageException {

    /** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -8043122842843683644L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}