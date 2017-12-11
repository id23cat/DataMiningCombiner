package evm.dmc.service;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.web.exceptions.ProjectNotFoundException;

public interface ProjectService {
	public void add(ProjectModel proModel);
	public ProjectModel getFirst();
	public ProjectModel getByName(String name) throws ProjectNotFoundException;

}
