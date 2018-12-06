package evm.dmc.webApi.exceptions;

import java.util.function.Supplier;

/**
 * Throwed when no account found
 */
public class AccountNotFoundException extends NotFoundException {

	/** Defined to avoid problems with serialization */
	private static final long serialVersionUID = 990656933748723543L;
	
	public static Supplier<AccountNotFoundException> supplier(Long id) {
		return ()-> new AccountNotFoundException("Account with id=" + id + " not found");
	}

	public AccountNotFoundException(String message){
		super(message);
	}

	public AccountNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
