package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;

public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
    Stream<Algorithm> findByNameIn(List<String> names);

    Stream<Algorithm> findByProject(ProjectModel project);

    Optional<Algorithm> findByProjectAndName(ProjectModel project, String name);

    Stream<Algorithm> findByProjectAndNameIn(ProjectModel project, Set<String> names);
//	Set<Algorithm> findByProjectAndNameIn(ProjectModel project, Set<String> names);

    void deleteByProjectAndNameIn(ProjectModel project, Set<String> names);

}
