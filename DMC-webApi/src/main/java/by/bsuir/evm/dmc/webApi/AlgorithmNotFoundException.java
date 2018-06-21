package by.bsuir.evm.dmc.webApi;

public class AlgorithmNotFoundException extends NotFoundException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6065449387879839019L;

	public AlgorithmNotFoundException(String message){
		super(message);
	}

	public AlgorithmNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
