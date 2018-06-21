package by.bsuir.evm.dmc.webApi;

import java.util.function.Supplier;

public class AccountNotFoundException extends NotFoundException {
	/**
	 * 
	 */
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
