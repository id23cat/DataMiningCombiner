package evm.dmc.web.exceptions;

public class UserNotExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 990656933748723543L;

	public UserNotExistsException(){
		super();
	}
	
	public UserNotExistsException(String message){
		super(message);
	}

	public UserNotExistsException(String message, Throwable cause){
		super(message, cause);
	}
}
