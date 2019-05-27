package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.account.Role;

public interface AccountRepository extends JpaRepository<Account, Long> {
    String FIND_ALL = "SELECT p FROM Account p ORDER BY userName ";

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUserName(String userName);

    List<Account> findByRole(Role role);

    @Query(FIND_ALL)
    Stream<Account> streamAll();

    Optional<Account> findById(Long id);
}
