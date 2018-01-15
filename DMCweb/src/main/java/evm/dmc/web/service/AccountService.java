package evm.dmc.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import evm.dmc.api.model.account.Account;

public interface AccountService extends UserDetailsService {
	Account save(Account account);
	
	Account getAccountByName(String username) throws UsernameNotFoundException;
	
	void signin(Account account);
	
	void delete(Account account);
	
	void refresh(Account account);
	
	Account merge(Account account);
}
