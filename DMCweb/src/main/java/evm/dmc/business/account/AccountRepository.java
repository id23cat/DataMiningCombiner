package evm.dmc.business.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Optional<Account> findByEmail(String email);
	
	Optional<Account> findByUserName(String userName);
	
//	@Async
	List<Account> findByRole(Role role);
	
//	@Async
	List<Account> findByRole(Role role, Pageable pageable);
}
