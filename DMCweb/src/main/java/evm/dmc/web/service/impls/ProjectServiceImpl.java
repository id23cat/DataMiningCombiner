package evm.dmc.web.service.impls;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Override
	public ProjectService save(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> projectRepo.saveAndFlush(model));
		if(!proModel.isPresent())
			log.warn("Trying to save empty ProjectModel");
		return this;
	}

	@Override
	public ProjectService delete(Optional<ProjectModel> proModel) {
		proModel.ifPresent((model) -> {
			projectRepo.delete(model);
			projectRepo.flush();
		});
		if(!proModel.isPresent())
			log.warn("Trying to delete empty ProjectModel");
		return this;
	}

	@Override
	public ProjectService delete(String name) {
		projectRepo.deleteByProjectName(name);
		projectRepo.flush();
		return this;
	}

	@Override
	public Stream<ProjectModel> getAll() {
		return projectRepo.straemAll();
	}

	@Override
	public Optional<ProjectModel> getByName(String name) {
		return projectRepo.findByProjectName(name);
	}
	
	@Override
	public Stream<ProjectModel> getByAccount(Account account) {
		return projectRepo.findAllByAccount(account);
	}

	@Override
	public ProjectModel getNew() {
		return new ProjectModel();
	}

	@Override
	public ProjectModel getNew(ProjectType type, Set<AlgorithmModel> algorithms,
			Properties properties, String projectName) {
		return new ProjectModel(type, algorithms, properties, projectName);
	}

}
