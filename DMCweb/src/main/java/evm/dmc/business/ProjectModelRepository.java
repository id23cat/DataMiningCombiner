package evm.dmc.business;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.ProjectModel;

public interface ProjectModelRepository extends JpaRepository<ProjectModel, Long> {
	Optional<ProjectModel> findByProjectName(String name);
}
