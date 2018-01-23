package evm.dmc.web.exceptions;

public class AlgorithmNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6065449387879839019L;

	public AlgorithmNotFoundException(){
		super();
	}
	
	public AlgorithmNotFoundException(String message){
		super(message);
	}

	public AlgorithmNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
