package evm.dmc.model.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.MetaData;

public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
	Stream<MetaData> findByProject(ProjectModel project);

}
