package evm.dmc.core;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.core.api.Project;
import evm.dmc.core.api.SimplestProject;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

/**
 * Contains only one implementation of algorithm, so usually use only first
 * element from list of AlgorithmModels from ProjectModel
 * 
 * @author id23cat
 * 
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SimplestProject
public class SimplestProjectImpl implements Project {
	public static final String PROJIMPL_NAME = "SimplestProjectImpl";
	private Algorithm algorithm = new SerialAlgorithm();
	private ProjectModel model = new ProjectModel();

	@Autowired
	private FrameworksRepository frameworkRepo;

	public SimplestProjectImpl() {
		model.setTypeName(PROJIMPL_NAME);
	}

	@PostConstruct
	protected void init() {
		algorithm.setFrameworksRepository(frameworkRepo);
		model.getAlgorithms().add(0, algorithm.getModel());
	}

	@Override
	public Project setModel(ProjectModel model) {
		this.model = model;
		createAlgorithm(model.getAlgorithms().get(0));
		return this;
	}

	@Override
	public ProjectModel getModel() {
		return model;
	}

	@Override
	public Algorithm createAlgorithm() {
		algorithm = new SerialAlgorithm(frameworkRepo);
		model.getAlgorithms().add(0, algorithm.getModel());

		return algorithm;
	}

	@Override
	public Algorithm createAlgorithm(AlgorithmModel algModel) {
		algorithm = new SerialAlgorithm(frameworkRepo, algModel);
		model.getAlgorithms().add(0, algorithm.getModel());

		return algorithm;
	}

	@Override
	public Algorithm getAlgorithm() {
		return algorithm;
	}

	@Override
	public Project setProjectName(String name) {
		model.setProjectName(name);
		return this;
	}

	@Override
	public String getPrijectName() {
		return model.getProjectName();
	}

	@Override
	public FrameworksRepository getFunctionsRepository() {
		return frameworkRepo;
	}

	@Override
	public Set<String> getFunctionsSet() {
		return frameworkRepo.getFunctionsDescriptors().keySet();
	}

	@Override
	public Set<String> getDataLoadersSet() {
		return frameworkRepo.getDataLoadersDescriptorsMap().keySet();
	}

	@Override
	public Set<String> getDataSaversSet() {
		return frameworkRepo.getDataSaversDescriptorsMap().keySet();
	}

	@Override
	public FunctionModel getFunctionModel(String descriptor) throws NoSuchFunctionException {
		return frameworkRepo.getFunction(descriptor).getFunctionModel();
	}

	@Override
	public FunctionSrcModel getDataSrcModel(String descriptor) throws NoSuchFunctionException {
		return frameworkRepo.getDataLoaderFunction(descriptor).getSrcModel();
	}

	@Override
	public FunctionDstModel getDataDstModel(String descriptor) throws NoSuchFunctionException {
		return frameworkRepo.getDataSaverFunction(descriptor).getDstModel();
	}

	@Override
	public Project setDataSrc(Algorithm algorithm, FunctionSrcModel srcModel) {
		algorithm.addDataSource(srcModel);
		return this;
	}

	@Override
	public Project setDataDst(Algorithm algorithm, FunctionDstModel dstModel) {
		algorithm.addDataDestination(dstModel);
		return this;
	}

	@Override
	public Project addFunction(Algorithm algorithm, FunctionModel funModel) throws NoSuchFunctionException {
		algorithm.addCommand(funModel);
		return this;
	}

	@Override
	public Project insertFunction(Algorithm algorithm, FunctionModel funModel, Integer position)
			throws NoSuchFunctionException {
		algorithm.insertCommand(funModel, position);
		return this;
	}

	@Override
	public Project insertFunctionAfter(Algorithm algorithm, FunctionModel funModel, FunctionModel after)
			throws NoSuchFunctionException {
		algorithm.insertCommandAfter(funModel, after);
		return this;
	}

	@Override
	public Project insertFunctionBefore(Algorithm algorithm, FunctionModel funModel, FunctionModel before)
			throws NoSuchFunctionException {
		algorithm.insertCommandAfter(funModel, before);
		return this;
	}

}
