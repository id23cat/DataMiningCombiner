package evm.dmc.web.service;

import java.util.Optional;
import java.util.Set;

import evm.dmc.api.model.ProjectModel;

public interface EnityGetter<T> {
	Optional<T> getByProjectAndName(ProjectModel project, String name);
	Set<T> getByProjectAndName(ProjectModel project, Set<String> names);
	
	Set<T> getForProject(ProjectModel project);
}
