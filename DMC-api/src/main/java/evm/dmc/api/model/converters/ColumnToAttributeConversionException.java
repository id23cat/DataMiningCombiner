package evm.dmc.api.model.converters;

public class ColumnToAttributeConversionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566041094197534195L;

	public ColumnToAttributeConversionException(String message) {
        super(message);
    }

    public ColumnToAttributeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
