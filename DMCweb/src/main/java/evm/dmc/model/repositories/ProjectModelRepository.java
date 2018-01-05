package evm.dmc.model.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;

public interface ProjectModelRepository extends JpaRepository<ProjectModel, Long> {
	public final static String FIND_ALL = "SELECT p FROM ProjectModel p";
	
	@Modifying
//	@Transactional
	Long deleteByProjectName(String name);
	
	Optional<ProjectModel> findByProjectName(String name);
	
	Stream<ProjectModel> findAllByAccount(Account account);
	
	@Query(FIND_ALL)
	Stream<ProjectModel> straemAll();
}
