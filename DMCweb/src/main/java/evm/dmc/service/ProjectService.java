package evm.dmc.service;

import evm.dmc.api.model.ProjectModel;

public interface ProjectService {
	public void add(ProjectModel proModel);
	public ProjectModel getFirst();

}
