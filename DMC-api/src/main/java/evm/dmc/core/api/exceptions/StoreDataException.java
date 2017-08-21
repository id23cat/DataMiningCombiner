package evm.dmc.core.api.exceptions;

import java.io.IOException;

public class StoreDataException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StoreDataException(Throwable exc) {
		super(exc);
	}

	public StoreDataException(String message, Throwable exc) {
		super(message, exc);
	}

}
