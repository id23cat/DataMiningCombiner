package evm.dmc.web.service;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;

public interface ProjectService {
	ProjectService save(Optional<ProjectModel> proModel);
	
	ProjectService delete(Optional<ProjectModel> proModel);
	ProjectService delete(String name);
	
	Stream<ProjectModel> getAll();
	Optional<ProjectModel> getByName(String name);
	
	ProjectModel getNew();
	ProjectModel getNew(ProjectType type, Set<AlgorithmModel> algorithms, Properties properties, String projectName);

}
