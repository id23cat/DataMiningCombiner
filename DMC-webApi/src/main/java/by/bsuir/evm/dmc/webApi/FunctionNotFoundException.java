package by.bsuir.evm.dmc.webApi;

public class FunctionNotFoundException extends NotFoundException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2662782984482995244L;

	public FunctionNotFoundException(String message){
		super(message);
	}

	public FunctionNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
