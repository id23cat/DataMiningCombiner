package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.MetaData;

public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
	Stream<MetaData> findByProject(ProjectModel project);
	List<MetaData> findByProject(ProjectModel project, Sort sort);
	
	Optional<MetaData> findByProjectAndName(ProjectModel project, String name);
	
//	MetaData findByProjectAndName(ProjectModel project, String name);
	
	Stream<MetaData> findByProjectAndNameIn(ProjectModel project, Set<String> names);
	
	void deleteByProjectAndNameIn(ProjectModel project, Set<String> names);
	
//	Stream<MetaData> findAllByProjectIdAndAccountId(Long projectId, Long accountId);
	Stream<MetaData> findAllByProjectId(Long projectId);
	
	Optional<MetaData> findById(Long id);
	
	Optional<MetaData> findByIdAndProjectId(Long id, Long projectId);

}
