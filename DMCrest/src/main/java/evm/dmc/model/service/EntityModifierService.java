package evm.dmc.model.service;

import java.util.Set;

import evm.dmc.api.model.ProjectModel;

public interface EntityModifierService<T> {

	T save(T entity);
	void delete(ProjectModel project, Set<String> names);
	void delete(T entity);
}
