package evm.dmc.web.service.impls;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.AlgorithmRepository;
import evm.dmc.web.exceptions.FunctionNotFoundException;
import evm.dmc.web.exceptions.MetaDataNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.FrameworkFrontendService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.ProjectService;
import evm.dmc.web.service.dto.TreeNodeDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlgorithmServiceImpl implements AlgorithmService {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
//	@Delegate(types = {AlgorithmRepository.class})
	AlgorithmRepository algorithmRepository;
	
	@Autowired
	MetaDataService metaDataService;
	
	@Autowired
	FrameworkFrontendService frameworkService;
	
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
	public Algorithm setDataSource(Algorithm algorithm, String datasetName) throws MetaDataNotFoundException {
		log.debug("Merge algorithm");
		algorithm = merge(algorithm);
		final ProjectModel project = algorithm.getProject();
		Optional<MetaData> optMeta = metaDataService.getByProjectAndName(project, datasetName);
		
		algorithm.setDataSource(optMeta.orElseThrow(() ->
				new MetaDataNotFoundException(
						"No such dataset {" + datasetName +
						"} for project " + project.getName())));
		return algorithm;
	}
	
	@Override
	public Optional<MetaData> getDataSource(Optional<Algorithm> optAlgorithm) {
		if(! optAlgorithm.isPresent()) {
			return Optional.ofNullable(null);
		}
		return optAlgorithm.map(alg -> alg.getDataSource());
	}
	
	@Override
	@Transactional
	public Algorithm setAttributes(Algorithm algorithm, MetaData metaData) {
		algorithm = merge(algorithm);
		if(algorithm.getDataSource() == null || 
				algorithm.getDataSource().getName() != metaData.getName()) {
			setDataSource(algorithm, metaData.getName());
		}
		if(algorithm.getDataSource().getAttributes().equals(metaData.getAttributes()))
			return algorithm;
		algorithm.setSrcAttributes(metaData.getAttributes());
		return algorithm;
	}
	
	@Override
	@Transactional
	public FWMethod addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction) {
		if(algorithm.getMethod() == null || algorithm.getMethod().getSteps().isEmpty()) {
			return addMethod(algorithm, dtoFunction, 0);
		} else {
			Integer size = algorithm.getMethod().getSteps().size();
			return addMethod(algorithm, dtoFunction, size);
		}
	}
	
	@Override
	@Transactional
	public FWMethod addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step) {
		algorithm = merge(algorithm);
		Optional<FunctionModel> optFunction = frameworkService.getFunction(dtoFunction.getId());
		FWMethod method = AlgorithmService.functionToFWMethod(optFunction.orElseThrow(() ->
				new FunctionNotFoundException(
						"Function with ID=" + dtoFunction.getId() + " not found")));
		
		if(algorithm.getMethod() == null)
			AlgorithmService.algorithmCreatePatternMethod(algorithm);

		PatternMethod rootMethod = algorithm.getMethod();
		step = step > rootMethod.getSteps().size() ? rootMethod.getSteps().size() : step;

		rootMethod.getSteps().add(step, method);
		
		return method;
	}
	
	@Override
	@Transactional
	public Algorithm save(Algorithm algorithm) {
		return algorithmRepository.save(algorithm);
	}
	
	@Override
	@Transactional
	public List<FrameworkModel> getFrameworksList() {
		return frameworkService.getFrameworksList();
	}
	
	@Override
	@Transactional
	public List<TreeNodeDTO> getFrameworksAsTreeNodes() {
		return frameworkService.getFrameworksAsTreeNodes();
	}

	private Algorithm merge(Algorithm algorithm) {
		return em.merge(algorithm);
	}
	
}
