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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.model.repositories.ProjectModelRepository;
import evm.dmc.web.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService{
	@Autowired
	private ProjectModelRepository projectRepo;
	
	@Autowired
	private EntityManager em;

	@Override
	@Transactional
	public ProjectService save(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> projectRepo.saveAndFlush(model));
		if(!proModel.isPresent())
			log.warn("Trying to save empty ProjectModel");
		return this;
	}
	
	@Override
	public ProjectModel merge(ProjectModel project) {
		return em.merge(project);
	}

	@Override
	@Transactional
	public ProjectService delete(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> {
			projectRepo.delete(model);
			projectRepo.flush();
		});
		if(!proModel.isPresent())
			log.error("Trying to delete empty Optional<ProjectModel>");
		return this;
	}

	@Override
	@Transactional
	public ProjectService delete(String name) {
		projectRepo.deleteByName(name);
		projectRepo.flush();
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
	@Transactional
	public Set<String> getNamesByAccount(Account account) {
		return getByAccount(account).map((proj) -> {return proj.getName();}).collect(Collectors.toSet());
	}

	@Override
	public ProjectModel getNew() {
		return new ProjectModel();
	}

	@Override
	public ProjectModel getNew(Account account, ProjectType type, Set<AlgorithmModel> algorithms,
			Properties properties, String projectName) {
		return new ProjectModel(account, type, algorithms, properties, projectName);
	}
	
	public static <T extends Collection<ProjectModel>> T getAsCollection(Stream<ProjectModel> stream, 
			Collector<ProjectModel,?,T> collector) {
		T projectsCollection = stream.collect(collector);
		stream.close();
		return projectsCollection;
	}

}
