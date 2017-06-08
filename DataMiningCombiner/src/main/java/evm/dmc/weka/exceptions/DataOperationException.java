package evm.dmc.weka.exceptions;

public class DataOperationException extends RuntimeException {

	public DataOperationException(Throwable cause) {
		super(cause);
	}

	public DataOperationException(String message) {
		super(message);
	}

}
