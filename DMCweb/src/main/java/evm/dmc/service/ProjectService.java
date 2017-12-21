package evm.dmc.service;

import java.util.Optional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.web.exceptions.ProjectNotFoundException;

public interface ProjectService {
	public void add(ProjectModel proModel);
	public ProjectModel getFirst();
	public Optional<ProjectModel> getByName(String name);

}
