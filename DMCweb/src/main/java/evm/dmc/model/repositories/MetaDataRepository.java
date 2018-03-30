package evm.dmc.model.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;

public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
	Stream<MetaData> findByProject(ProjectModel project);
	
	Optional<MetaData> findByProjectAndName(ProjectModel project, String name);
	
//	MetaData findByProjectAndName(ProjectModel project, String name);
	
	Stream<MetaData> findByProjectAndNameIn(ProjectModel project, Set<String> names);
	
	void deleteByProjectAndNameIn(ProjectModel project, Set<String> names);

}
