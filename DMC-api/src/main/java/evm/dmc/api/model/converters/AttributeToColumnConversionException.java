package evm.dmc.api.model.converters;

public class AttributeToColumnConversionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566041094197534195L;

	public AttributeToColumnConversionException(String message) {
        super(message);
    }
	
	public AttributeToColumnConversionException(Throwable cause) {
        super(cause);
    }

    public AttributeToColumnConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
