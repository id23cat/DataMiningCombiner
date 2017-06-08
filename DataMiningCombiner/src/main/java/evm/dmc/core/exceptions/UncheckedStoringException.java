package evm.dmc.core.exceptions;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UncheckedStoringException extends UncheckedIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4887174710696024660L;

	public UncheckedStoringException(IOException cause) {
		super(cause);
	}

	public UncheckedStoringException(String message, IOException exc) {
		super(message, exc);
	}

	public UncheckedStoringException(String message) {
		super(message, new IOException());

	}

}
