package evm.dmc.service;

import evm.dmc.api.model.ProjectModel;

public interface ProjectService {
	public void addNew(ProjectModel proModel);
	public ProjectModel getFirst();

}
