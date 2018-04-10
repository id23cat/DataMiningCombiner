package evm.dmc.web.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;

public interface EnityGetter<T> {
	Optional<T> getByProjectAndName(ProjectModel project, String name);
	Set<T> getByProjectAndName(ProjectModel project, Set<String> names);
	
	Set<T> getForProject(ProjectModel project);
	
	@Transactional(readOnly=true)
	default List<T> getForProjectSortedBy(ProjectModel project, String fieldName) {
		List<T> list = new LinkedList<>(getForProject(project));
		BeanComparator<T> comparator = new BeanComparator<>(fieldName);
		list.sort((v1, v2) -> comparator.compare(v1, v2));
		return list;
	}
}
