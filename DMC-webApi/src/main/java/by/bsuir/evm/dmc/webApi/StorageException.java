package by.bsuir.evm.dmc.webApi;

public class StorageException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3268906588922725839L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
