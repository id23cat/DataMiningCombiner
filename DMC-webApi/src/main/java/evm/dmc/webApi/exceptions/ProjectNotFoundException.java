package evm.dmc.webApi.exceptions;

import java.util.function.Supplier;

/**
 * Throwed when no Project model found
 */
public class ProjectNotFoundException extends NotFoundException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = -2661762885454572655L;
	
	public static Supplier<AccountNotFoundException> supplier(Long id) {
		return ()-> new AccountNotFoundException("Project with id=" + id + " not found");
	}

	public ProjectNotFoundException(String message) {
		super(message);
	}
	
	public ProjectNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
