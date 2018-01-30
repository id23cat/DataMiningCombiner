package evm.dmc.web.exceptions;

public class UnsupportedFileTypeException extends StorageException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8043122842843683644L;

	public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}