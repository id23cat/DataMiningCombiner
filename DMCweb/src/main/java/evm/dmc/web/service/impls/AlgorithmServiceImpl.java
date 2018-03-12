package evm.dmc.web.service.impls;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.AlgorithmFunction;
import evm.dmc.api.model.algorithm.SubAlgorithm;
import evm.dmc.model.repositories.AlgorithmRepository;
import evm.dmc.web.service.AlgorithmService;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
	
	@Autowired
	AlgorithmRepository algRepository;
	
	@Autowired
	EntityManager em;
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Algorithm> getByNameAndParentProject(String name, ProjectModel project) {
		return getByNameAndParentProject(Stream.of(name).collect(Collectors.toSet()), project)
				.findFirst();
	}
	
	@Override
	public Optional<Algorithm> getByNameAndDependentProject(String name, ProjectModel project) {
		return getByNameAndDependentProject(Stream.of(name).collect(Collectors.toSet()), project)
				.findFirst();
	}

	@Override
	public Stream<Algorithm> getByNameAndParentProject(Set<String> names, ProjectModel project) {
		return algRepository.findByParentProjectAndNameIn(project, names);
	}

	@Override
	public Stream<Algorithm> getByNameAndDependentProject(Set<String> names, ProjectModel project) {
		return algRepository.findByDependentProjectsAndNameIn(project, names);
	}
	
	@Override
	@Transactional
	public void delete(Algorithm algorithm) {
		algRepository.delete(algorithm);
	}
	
	@Override
	@Transactional
	public Algorithm save(Algorithm algorithm) {
		return algRepository.save(algorithm);
	}

	@Override
	@Transactional
	public ProjectModel addDependentProject(Algorithm algorithm, ProjectModel project) {
		algorithm = merge(algorithm);
		project = merge(project);
		
		algorithm.getDependentProjects().add(project);
		project.getAlgorithms().add(algorithm);
		return project;
	}

	private Algorithm merge(Algorithm algorithm) {
		return em.merge(algorithm);
	}
	
	private ProjectModel merge(ProjectModel project) {
		return em.merge(project);
	}

}
