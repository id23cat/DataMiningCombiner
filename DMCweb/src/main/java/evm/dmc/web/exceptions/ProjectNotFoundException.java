package evm.dmc.web.exceptions;

public class ProjectNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 990656933748723543L;

	public ProjectNotFoundException(){
		super();
	}
	
	public ProjectNotFoundException(String message){
		super(message);
	}

	public ProjectNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
