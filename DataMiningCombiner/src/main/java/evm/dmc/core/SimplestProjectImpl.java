package evm.dmc.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.core.api.Project;

/**
 * Contains only one implementation of algorithm, so usually use only first
 * element from list of AlgorithmModels from ProjectModel
 * 
 * @author id23cat
 * 
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimplestProjectImpl implements Project {
	public static final String PROJIMPL_NAME = "SimplestProjectImpl";
	private Algorithm algorithm = new SerialAlgorithm();
	private ProjectModel model = new ProjectModel();

	@Autowired
	private FrameworksRepository frameworkRepo;

	public SimplestProjectImpl() {
		model.setName(PROJIMPL_NAME);
	}

	@PostConstruct
	protected void init() {
		algorithm.setFrameworksRepository(frameworkRepo);
		model.getAlgorithm().add(0, algorithm.getModel());
	}

	@Override
	public Project setModel(ProjectModel model) {
		this.model = model;
		createAlgorithm(model.getAlgorithm().get(0));
		return this;
	}

	@Override
	public ProjectModel getModel() {
		return model;
	}

	@Override
	public Algorithm createAlgorithm() {
		algorithm = new SerialAlgorithm(frameworkRepo);
		model.getAlgorithm().add(0, algorithm.getModel());
		
		return algorithm;
	}

	@Override
	public Algorithm createAlgorithm(AlgorithmModel algModel) {
		algorithm = new SerialAlgorithm(frameworkRepo, algModel);
		model.getAlgorithm().add(0, algorithm.getModel());
		
		return algorithm;
	}

	@Override
	public Algorithm getAlgorithm() {
		return algorithm;
	}

}
