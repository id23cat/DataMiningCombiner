package evm.dmc.web.service.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.ProjectModelRepository;
import evm.dmc.web.exceptions.EntityNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@Service("projectService")
@Slf4j
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectModelRepository projectRepo;
	
//	@Autowired
//	private AlgorithmService algorithmService;
	
	@Autowired
	private MetaDataService metaDataService;
	
	@Autowired
	private EntityManager em;
	
	@Override
	@Transactional
	public ProjectService save(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> save(model));
		if(!proModel.isPresent())
			log.warn("Trying to save empty ProjectModel");
		return this;
	}
	
	@Override
	@Transactional
	public ProjectModel save(ProjectModel proModel) {
		return projectRepo.save(proModel);
	}
	

	@Override
	@Transactional
	public ProjectService delete(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> {
			delete(model);
		});
		if(!proModel.isPresent())
			log.error("Trying to delete empty Optional<ProjectModel>");
		return this;
	}
	
	@Override
	@Transactional
	public ProjectService delete(ProjectModel proModel) {
		projectRepo.delete(proModel);
//		projectRepo.flush();
		return this;
	}

	@Override
	@Transactional
	public ProjectService deleteByName(String name) {
		projectRepo.deleteByName(name);
		projectRepo.flush();
		return this;
	}
	
	@Override
	@Transactional
	public ProjectService deleteByAccountAndNames(Account account, Set<String> names) {
		projectRepo.deleteByAccountAndNameIn(account, names);
		return this;
		
	}
	
	@Override
	@Transactional
	public ProjectService deleteAllByNames(List<String> names){
		log.debug("Delete by names: {}", names);
		projectRepo.deleteByNameIn(names);
		return this;
	}

	@Override
	@Transactional(readOnly = true)
	public Stream<ProjectModel> getAll() {
		return projectRepo.straemAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProjectModel> getAllAsList() {
		return getAsCollection(getAll(), Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<ProjectModel> getAllAsSet() {
		return getAsCollection(getAll(), Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Stream<ProjectModel> getByName(String name) {
		return projectRepo.findByName(name);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Optional<ProjectModel> getById(Long id) {
		Optional<ProjectModel> optProject = Optional.empty();
		try {
			optProject = Optional.ofNullable(projectRepo.findOne(id));
		} catch (EntityNotFoundException exc) {
			optProject = Optional.empty();
		}
		
		return optProject;
	}
	
	@Override
	@Transactional
	public ProjectModel getOrSave(final ProjectModel project) {
		return getById(project.getId()).orElseGet(()->{return save(project);});
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<ProjectModel> getByNameAndAccount(String name, Account account) {
		return projectRepo.findByNameAndAccount(name, account);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Stream<ProjectModel> getByAccount(Account account) {
		return projectRepo.findAllByAccount(account);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<ProjectModel> getByAccountAsSet(Account account) {
//		Set<ProjectModel> projectsSet;
//		try(Stream<ProjectModel> pros = getByAccount(account)) {
//			projectsSet = pros.collect(Collectors.toSet());
//		}
//		return projectsSet;
		return getAsCollection(getByAccount(account), Collectors.toSet());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProjectModel> getByAccountAsList(Account account) {
//		List<ProjectModel> projectsList;
//		try(Stream<ProjectModel> pros = getByAccount(account)) {
//			projectsList = pros.collect(Collectors.toList());
//		}
//		return projectsList;
		
		return getAsCollection(getByAccount(account), Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<String> getNamesByAccount(Account account) {
		return getByAccount(account).map((proj) -> {return proj.getName();}).collect(Collectors.toSet());
	}

	@Override
	public ProjectModel getNew() {
		return new ProjectModel();
	}

	@Override
	public ProjectModel getNew(Account account, ProjectType type, Set<Algorithm> algorithms,
			Properties properties, String projectName) {
		return new ProjectModel(account, type, algorithms, properties, projectName);
	}
	
//	@Override
////	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	@Transactional
//	public Algorithm assignAlgorithm(ProjectModel project, Algorithm algorithm) {
//		
//		project = merge(project);
////		project = save(project);
//		
////		project.assignAlgorithm(algorithm);
////		algorithm.getDependentProjects().remove(project);
//		project.getAlgorithms().add(algorithm);
//		algorithm.setParentProject(project);
//		
//		return algorithmService.save(algorithm);
//	}
	
//	@Override
//	@Transactional
//	public Algorithm addAlgorithm(ProjectModel project, Algorithm algorithm) {
//		if(algorithm.getParentProject() == null) {
//			return assignAlgorithm(project, algorithm);
//		}
//		project = merge(project);
////		algorithm.getDependentProjects().add(project);
//		project.getAlgorithms().add(algorithm);
//		
//		return algorithm;
//	}
	
//	@Override
////	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	@Transactional
//	public Algorithm addAlgorithm(ProjectModel project, Algorithm algorithm) {
//		project = merge(project);
//		project.getAlgorithms().add(algorithm);
//		algorithm.setProject(project);
//		
//		return algorithmService.save(algorithm);
//	}
	
//	@Override
//	@Transactional
//	@Modifying
//	public ProjectModel deleteAlgorithm(ProjectModel project, Algorithm algorithm) {
//		project = merge(project);
////		Optional<ProjectModel> newParrentProject = algorithm.getDependentProjects().stream().findFirst();
//		project.getAlgorithms().remove(algorithm);
//		log.debug("Algorithms set after removing algorithm: {}", project.getAlgorithms().size());
////		if(newParrentProject.isPresent()) {
////			algorithm = assignAlgorithm(newParrentProject.get(), algorithm);
////		} else {
////			algorithmService.delete(algorithm);
////		}
//		algorithmService.delete(algorithm);
//		
//		return project;
//	}
	
//	@Override
//	@Transactional
//	@Modifying
//	public ProjectModel deleteAlgorithms(ProjectModel project, Set<String> names) {
//		project = merge(project);
//		
//		Set<Algorithm> algorithms = algorithmService.getByNameAndProject(names, project).collect(Collectors.toSet());
//		log.debug("Algorithmds for deletion: {}", algorithms.stream().map(alg -> alg.getName()).collect(Collectors.toSet()));
//		
////		projectRepo.flush();
//		for(Algorithm alg: algorithms) {
//			deleteAlgorithm(project, alg);
//		}
////		save(project);
//		
//		return project;
//	}
	
	@Override
	@Transactional
	public MetaData persistNewData(ProjectModel project, MetaData data) {
		project = merge(project);
		project.addMetaData(data);
//		save(project);
//		return findMetaDataByName(project, data.getName())
//				.orElseThrow(
//						() -> new EntityNotFoundException(
//								new StringBuilder("MetaData with name ")
//									.append(data.getName())
//									.append(" not found")
//									.toString())
//				);
		return metaDataService.save(data);
	}
	
	@Override
	public Algorithm getNewAlgorithm() {
		return AlgorithmService.getNewAlgorithm();
	}
	
	public static <T extends Collection<ProjectModel>> T getAsCollection(Stream<ProjectModel> stream, 
			Collector<ProjectModel,?,T> collector) {
		T projectsCollection = stream.collect(collector);
		stream.close();
		return projectsCollection;
	}
	
	
	public Optional<Algorithm> findAgorithmbyName(ProjectModel project, String algName) {
		return project.getAlgorithms().stream()
				.filter(prj -> prj.getName().equals(algName)).findAny();
	}
	
	public Optional<MetaData> findMetaDataByName(ProjectModel project, String dataName) {
		return project.getDataSources().stream()
				.filter(data -> data.getName().equals(dataName)).findAny();
	}
	
//	@Override
	private ProjectModel merge(ProjectModel project) {
		return em.merge(project);
//		if(project.getId() == null)
//			return null;
//		return projectRepo.getOne(project.getId());
	}

//	@Transactional(readOnly = true)
//	@Override
//	public Stream<Algorithm> getAllAlgorithms(ProjectModel project) {
//		project = merge(project);
//		return project.getAlgorithms().stream();
//	}

	@Transactional(readOnly = true)
	@Override
	public Stream<MetaData> getAllData(ProjectModel project) {
		project = merge(project);
		return project.getDataSources().stream();
	}


	
}
