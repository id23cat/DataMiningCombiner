package evm.dmc.webApi.exceptions;

import org.springframework.dao.DataAccessException;

/**
 * Parent exception for all exception that caused by nonexistence of requested data
 */
public abstract class NotFoundException extends DataAccessException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -9042102628034582981L;
	
	public NotFoundException(String message){
		super(message);
	}

	public NotFoundException(String message, Throwable cause){
		super(message, cause);
	}

}
