package evm.dmc.model.repositories;

import evm.dmc.api.model.ProjectModel;

public interface ProjectRepository {
	public void addNew(ProjectModel proModel);
	public ProjectModel getFirst();

}
