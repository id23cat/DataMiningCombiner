package evm.dmc.web.exceptions;

public class AccountNotFoundException extends NotFoundException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 990656933748723543L;

	public AccountNotFoundException(String message){
		super(message);
	}

	public AccountNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
