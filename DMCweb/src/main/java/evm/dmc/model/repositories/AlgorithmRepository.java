package evm.dmc.model.repositories;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;

public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
	Stream<Algorithm> findByNameIn(List<String> names);
	
	Stream<Algorithm> findByParentProjectAndNameIn(ProjectModel project, Set<String> names);
	
	Stream<Algorithm> findByDependentProjectsAndNameIn(ProjectModel project, Set<String> names);

}
