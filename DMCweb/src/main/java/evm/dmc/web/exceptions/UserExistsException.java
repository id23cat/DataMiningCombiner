package evm.dmc.web.exceptions;

public class UserExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 990656933748723543L;

	public UserExistsException(){
		super();
	}
	
	public UserExistsException(String message){
		super(message);
	}

	public UserExistsException(String message, Throwable cause){
		super(message, cause);
	}
}
