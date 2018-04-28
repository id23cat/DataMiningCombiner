package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;

public interface ProjectModelRepository extends JpaRepository<ProjectModel, Long> {
	public final static String FIND_ALL = "SELECT p FROM ProjectModel p";
	
	Long deleteByName(String name);
	
	Long deleteByNameIn(List<String> name);
	
	Long deleteByAccountAndNameIn(Account account, Set<String> names);
	
	Stream<ProjectModel> findByName(String name);
	
	Optional<ProjectModel> findByNameAndAccount(String name, Account account);
	
//	Optional<ProjectModel> findById(Long id);
	
	Stream<ProjectModel> findAllByAccount(Account account);
	
	@Query(FIND_ALL)
	Stream<ProjectModel> straemAll();
}
