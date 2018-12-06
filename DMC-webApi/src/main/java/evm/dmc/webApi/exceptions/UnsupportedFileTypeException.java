package evm.dmc.webApi.exceptions;

/**
 * Throwed when unsupported file type is used
 */
public class UnsupportedFileTypeException extends StorageException {

    /** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -8043122842843683644L;

	public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}