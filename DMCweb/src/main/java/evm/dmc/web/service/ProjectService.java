package evm.dmc.web.service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;

public interface ProjectService {
	ProjectService save(Optional<ProjectModel> proModel);
	ProjectService save(ProjectModel proModel);
	
//	ProjectModel merge(ProjectModel project);
	
	ProjectService delete(Optional<ProjectModel> proModel);
	ProjectService delete(ProjectModel proModel);
	ProjectService deleteByName(String name);
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
	
	AlgorithmModel assignAlgorithm(ProjectModel project, AlgorithmModel algorithm);
	
	ProjectModel delAlgorithmsByNames(ProjectModel project, String[] names);
	
	ProjectModel getNew();
	ProjectModel getNew(Account account, ProjectType type, Set<AlgorithmModel> algorithms, Properties properties, String projectName);
	
	AlgorithmModel getNewAlgorithm();
	
	MetaData addDataStorage(ProjectModel project, MetaData data);

}
