package evm.dmc.model.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.webApi.exceptions.ProjectNotFoundException;

public interface AccountService extends UserDetailsService {

	Optional<Account> save(Account account);
	Optional<Account> getAccountByName(String username) throws UsernameNotFoundException;
	Optional<Account> get(Long id);
	void signin(Account account);
	void delete(Account account);
	ProjectModel addProject(Account account, ProjectModel project) throws ProjectNotFoundException;
	Account delProject(Account account, ProjectModel project);
	Account delProjectsByNames(Account account, Set<String> names);
	Optional<ProjectModel> findProjectByName(Account account, String name);
	Stream<Account> getAll();
	List<Account> getAllAsList();
}
