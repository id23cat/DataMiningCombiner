package evm.dmc.core.exceptions;

import java.io.IOException;

public class StoringException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StoringException(Throwable exc) {
		super(exc);
	}

	public StoringException(String message, Throwable exc) {
		super(message, exc);
	}

}
