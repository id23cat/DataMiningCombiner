package evm.dmc.core.api.exceptions;

public class IndexOutOfRange extends IllegalArgumentException {
	public IndexOutOfRange(Throwable exc) {
		super(exc);
	}

	public IndexOutOfRange(String message) {
		super(message);
	}

}
