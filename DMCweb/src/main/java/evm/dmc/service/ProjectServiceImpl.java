package evm.dmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.Project;
import evm.dmc.core.api.SimplestProject;
import evm.dmc.model.repositories.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	private ProjectRepository projRepository;
	private Project project;
	
	@Autowired
	public ProjectServiceImpl(ProjectRepository repo, @SimplestProject Project proj) {
		this.projRepository = repo;
		this.project = proj;
	}
	
	@Override
	public void add(ProjectModel proModel){
		projRepository.add(proModel);
	}
	
	@Override
	public ProjectModel getFirst() {
		return projRepository.getFirst();
	}
	

}
