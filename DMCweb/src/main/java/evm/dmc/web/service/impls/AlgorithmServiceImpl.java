package evm.dmc.web.service.impls;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.AlgorithmFunction;
import evm.dmc.api.model.algorithm.SubAlgorithm;
import evm.dmc.model.repositories.AlgorithmRepository;
import evm.dmc.web.service.AlgorithmService;

public class AlgorithmServiceImpl implements AlgorithmService {
	
	@Autowired
	AlgorithmRepository algRepository;
	
	@Autowired
	EntityManager em;

	@Override
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
