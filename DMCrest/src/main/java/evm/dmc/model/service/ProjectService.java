package evm.dmc.model.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;

public interface ProjectService {
	ProjectService save(Optional<ProjectModel> proModel);
	ProjectModel save(ProjectModel proModel);
	
	ProjectModel addProject(Long accountId, ProjectModel project);
	
	ProjectService delete(Optional<ProjectModel> proModel);
	ProjectService delete(ProjectModel proModel);
	ProjectService deleteByName(String name);
	ProjectService deleteByAccountAndNames(Account account, Set<String> names);
	ProjectService deleteAllByNames(List<String> names);
	ProjectService deleteById(Account account, Long id);
	
	Stream<ProjectModel> getAll();
	List<ProjectModel> getAllAsList();
	Set<ProjectModel> getAllAsSet();
	
	Stream<ProjectModel> getByName(String name);
	Optional<ProjectModel> getById(Long id);
	Optional<ProjectModel> getByNameAndAccount(String name, Account account);
	ProjectModel getOrSave(ProjectModel project);
	
	Stream<ProjectModel> getByAccount(Account account);
	Set<ProjectModel> getByAccountAsSet(Account account);
	List<ProjectModel> getByAccountAsList(Account account);
	
	Set<String> getNamesByAccount(Account account);
	
	
	ProjectModel getNew();
	
	Stream<MetaData> getAllData(ProjectModel project);
	MetaData persistNewData(ProjectModel project, MetaData data);

}
