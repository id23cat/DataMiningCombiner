package evm.dmc.web.service;

import java.util.Set;

import evm.dmc.api.model.ProjectModel;

public interface EntityModifier<T> {

    T save(T entity);

    void delete(ProjectModel project, Set<String> names);

    void delete(T entity);
}
