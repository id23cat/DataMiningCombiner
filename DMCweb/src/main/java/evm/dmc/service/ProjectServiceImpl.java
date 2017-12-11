package evm.dmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.business.ProjectModelRepository;
import evm.dmc.core.api.Project;
import evm.dmc.core.api.SimplestProject;
import evm.dmc.model.repositories.ProjectRepository;
import evm.dmc.web.exceptions.ProjectNotFoundException;

@Service
public class ProjectServiceImpl implements ProjectService{
	@Autowired
	private ProjectModelRepository projectMRepository;

	private ProjectRepository projectRepository;
	private Project project;
	
	@Autowired
	public ProjectServiceImpl(ProjectRepository repo, @SimplestProject Project proj) {
		this.projectRepository = repo;
		this.project = proj;
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
	public ProjectModel getByName(String name) throws ProjectNotFoundException {
		return projectMRepository.findByProjectName(name)
				.orElseThrow(()-> new ProjectNotFoundException(String.format("Project %s not found", name)));
	}
	

}
