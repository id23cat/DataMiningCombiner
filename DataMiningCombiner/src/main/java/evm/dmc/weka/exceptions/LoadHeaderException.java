package evm.dmc.weka.exceptions;

import evm.dmc.core.api.exceptions.LoadDataException;

public class LoadHeaderException extends LoadDataException {

	public LoadHeaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadHeaderException(String message) {
		super(message);
	}

	public LoadHeaderException(Throwable cause) {
		super(cause);
	}

}
