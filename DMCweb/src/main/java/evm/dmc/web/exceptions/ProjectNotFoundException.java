package evm.dmc.web.exceptions;

public class ProjectNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2661762885454572655L;

	public ProjectNotFoundException(String message) {
		super(message);
	}
	
	public ProjectNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
