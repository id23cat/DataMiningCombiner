package evm.dmc.web.exceptions;

public class DataStructureException extends StorageException {

    /**
     *
     */
    private static final long serialVersionUID = 8062739962560746393L;

    public DataStructureException(String message) {
        super(message);
    }

    public DataStructureException(String message, Throwable cause) {
        super(message, cause);
    }

}
