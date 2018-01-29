package evm.dmc.web.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;

public interface AccountService extends UserDetailsService {
	Account save(Account account);
	
	Account getAccountByName(String username) throws UsernameNotFoundException;
	
	void signin(Account account);
	
	void delete(Account account);
	
//	void refresh(Account account);
//	
//	Account merge(Account account);
	
	ProjectModel addProject(Account account, ProjectModel project);
	
	Account delProject(Account account, ProjectModel project);
	
	Account delProjectsByNames(Account account, String[] names);

	Optional<ProjectModel> findProjectByName(Account account, String name);
}
