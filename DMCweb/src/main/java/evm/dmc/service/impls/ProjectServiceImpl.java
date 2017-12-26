package evm.dmc.service.impls;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.business.ProjectModelRepository;
import evm.dmc.model.repositories.ProjectRepository;
import evm.dmc.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService{
	@Autowired
	private ProjectModelRepository projectMRepository;

	private ProjectRepository projectRepository;
	
	@Autowired
	public ProjectServiceImpl(ProjectRepository repo) {
		this.projectRepository = repo;
	}
	
	@Override
	public void add(ProjectModel proModel){
		projectRepository.add(proModel);
	}
	
	@Override
	public ProjectModel getFirst() {
		return projectRepository.getFirst();
	}

	@Override
	public Optional<ProjectModel> getByName(String name) {
		return projectMRepository.findByProjectName(name);
	}
	
}
