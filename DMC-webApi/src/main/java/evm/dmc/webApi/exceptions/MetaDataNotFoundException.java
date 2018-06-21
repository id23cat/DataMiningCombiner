package evm.dmc.webApi.exceptions;

public class MetaDataNotFoundException extends NotFoundException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7557190537228887032L;

	public MetaDataNotFoundException(String message) {
		super(message);
	}

	public MetaDataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
