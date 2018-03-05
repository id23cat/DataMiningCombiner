package evm.dmc.web.service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;

public interface ProjectService {
	ProjectService save(Optional<ProjectModel> proModel);
	ProjectModel save(ProjectModel proModel);
	
//	ProjectModel merge(ProjectModel project);
	
	ProjectService delete(Optional<ProjectModel> proModel);
	ProjectService delete(ProjectModel proModel);
	ProjectService deleteByName(String name);
	ProjectService deleteByAccountAndNames(Account account, Set<String> names);
	ProjectService deleteAllByNames(List<String> names);
	
	Stream<ProjectModel> getAll();
	List<ProjectModel> getAllAsList();
	Set<ProjectModel> getAllAsSet();
	
	Stream<ProjectModel> getByName(String name);
	Optional<ProjectModel> getByNameAndAccount(String name, Account account);
	
	Stream<ProjectModel> getByAccount(Account account);
	Set<ProjectModel> getByAccountAsSet(Account account);
	List<ProjectModel> getByAccountAsList(Account account);
	
	Set<String> getNamesByAccount(Account account);
	
	Algorithm assignAlgorithm(ProjectModel project, Algorithm algorithm);
	Algorithm addAlgorithm(ProjectModel project, Algorithm algorithm);
	ProjectModel deleteAlgorithm(ProjectModel project, Algorithm algorithm);
	ProjectModel deleteAlgorithms(ProjectModel project, Set<String> names);
	
	ProjectModel getNew();
	ProjectModel getNew(Account account, ProjectType type, Set<Algorithm> algorithms, Properties properties, String projectName);
	Algorithm getNewAlgorithm();
	
	MetaData persistNewData(ProjectModel project, MetaData data);

}
