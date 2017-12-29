package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.Role;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByEmail(String email);
	
	Optional<Account> findByUserName(String userName);
	
//	@Async
	List<Account> findByRole(Role role);
	
//	@Async
	List<Account> findByRole(Role role, Pageable pageable);
}
