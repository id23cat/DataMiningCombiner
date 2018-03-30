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
import evm.dmc.model.repositories.AlgorithmRepository;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.ProjectService;
import lombok.experimental.Delegate;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
//	@Delegate(types = {AlgorithmRepository.class})
	AlgorithmRepository algorithmRepository;
	
	@Autowired
	EntityManager em;
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Algorithm> getByProjectAndName(ProjectModel project, String name) {
		return algorithmRepository.findByProjectAndName(project, name);
	}

	@Override
	@Transactional(readOnly=true)
	public Set<Algorithm> getByProjectAndName(ProjectModel project, Set<String> names) {
//		return algorithmRepository.findByProjectAndNameIn(project, names).stream();
		return algorithmRepository.findByProjectAndNameIn(project, names).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly=true)
	public Set<Algorithm> getForProject(ProjectModel project) {
		return algorithmRepository.findByProject(project).collect(Collectors.toSet());
	}
	
	@Override
	@Transactional
	public void delete(Algorithm algorithm) {
		algorithmRepository.delete(algorithm);
	}
	
	@Override
	@Transactional
	public void delete(ProjectModel project, Set<String> algorithmNames) {
		algorithmRepository.deleteByProjectAndNameIn(project, algorithmNames);
	}
	
	@Override
	@Transactional
	public Algorithm addNew(ProjectModel project, Algorithm algorithm) {
		project = projectService.getOrSave(project);
		algorithm.setProject(project);
		project.getAlgorithms().add(algorithm);
		return algorithmRepository.save(algorithm);
	}
	
	@Override
	@Transactional
	public Algorithm save(Algorithm algorithm) {
		return algorithmRepository.save(algorithm);
	}

	private Algorithm merge(Algorithm algorithm) {
		return em.merge(algorithm);
	}
	
}
