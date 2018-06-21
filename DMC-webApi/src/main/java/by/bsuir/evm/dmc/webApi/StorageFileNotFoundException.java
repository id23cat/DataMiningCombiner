package by.bsuir.evm.dmc.webApi;

public class StorageFileNotFoundException extends StorageException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8043122842843683644L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}