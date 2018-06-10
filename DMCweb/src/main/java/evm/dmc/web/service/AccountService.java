package evm.dmc.web.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.ProjectNotFoundException;

public interface AccountService extends UserDetailsService {
	Account save(Account account);
	
	Account getAccountByName(String username) throws UsernameNotFoundException;
	
	void signin(Account account);
	
	void delete(Account account);
	
//	void refresh(Account account);
//	
//	Account merge(Account account);
	
	ProjectModel addProject(Account account, ProjectModel project) throws ProjectNotFoundException;
	
	Account delProject(Account account, ProjectModel project);
	
	Account delProjectsByNames(Account account, Set<String> names);

	Optional<ProjectModel> findProjectByName(Account account, String name);
	
	Stream<Account> getAll();
}
