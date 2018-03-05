package evm.dmc.model.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;

public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
	List<Algorithm> findByNameIn(List<String> names);
	
	Set<Algorithm> findByParentProjectAndNameIn(ProjectModel project, Set<String> names);
	
	Set<Algorithm> findByDependentProjectsAndNameIn(ProjectModel project, Set<String> names);

}
