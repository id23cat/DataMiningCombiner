package evm.dmc.core.api.exceptions;

public class DataOperationException extends RuntimeException {

	public DataOperationException(Throwable cause) {
		super(cause);
	}

	public DataOperationException(String message) {
		super(message);
	}

}
