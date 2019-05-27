package evm.dmc.model.service.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.repositories.ProjectModelRepository;
import evm.dmc.model.service.MetaDataService;
import evm.dmc.model.service.ProjectService;
import evm.dmc.webApi.exceptions.AccountNotFoundException;
import evm.dmc.webApi.exceptions.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service("projectService")
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectModelRepository projectRepo;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @Override
    @Transactional
    public ProjectService save(Optional<ProjectModel> proModel) {
        proModel.ifPresent(this::save);
        if (!proModel.isPresent())
            log.warn("Trying to save empty ProjectModel");
        return this;
    }

    @Override
    @Transactional
    public ProjectModel save(ProjectModel proModel) {
        return projectRepo.save(proModel);
    }

    @Override
    @Transactional
    public ProjectModel addProject(Long accountId, ProjectModel project) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException.supplier(accountId));
        project.setAccount(account);
        account.getProjects().add(project);
        return project;

    }

    @Override
    @Transactional
    public ProjectService delete(Optional<ProjectModel> proModel) {
        proModel.ifPresent(this::delete);
        if (!proModel.isPresent())
            log.error("Trying to delete empty Optional<ProjectModel>");
        return this;
    }

    @Override
    @Transactional
    public ProjectService delete(ProjectModel proModel) {
        projectRepo.delete(proModel);
        return this;
    }

    @Override
    @Transactional
    public ProjectService deleteByName(String name) {
        projectRepo.deleteByName(name);
        projectRepo.flush();
        return this;
    }

    @Override
    @Transactional
    public ProjectService deleteByAccountAndNames(Account account, Set<String> names) {
        projectRepo.deleteByAccountAndNameIn(account, names);
        return this;

    }

    @Override
    @Transactional
    public ProjectService deleteAllByNames(List<String> names) {
        log.debug("Delete by names: {}", names);
        projectRepo.deleteByNameIn(names);
        return this;
    }

    @Override
    @Transactional
    public ProjectService deleteById(Account account, Long id) {
        projectRepo.deleteByAccountAndId(account, id);
        return this;
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<ProjectModel> getAll() {
        return projectRepo.straemAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectModel> getAllAsList() {
        return getAsCollection(getAll(), Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ProjectModel> getAllAsSet() {
        return getAsCollection(getAll(), Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<ProjectModel> getByName(String name) {
        return projectRepo.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectModel> getById(Long id) throws ProjectNotFoundException {
        Optional<ProjectModel> optProject;
        try {
            optProject = Optional.ofNullable(
                    projectRepo.findById(id)
                            .orElseThrow(ProjectNotFoundException.supplier(id))
            );
        } catch (ProjectNotFoundException exc) {
            optProject = Optional.empty();
        }

        return optProject;
    }

    @Override
    @Transactional
    public ProjectModel getOrSave(final ProjectModel project) {
        return getById(project.getId()).orElseGet(() -> save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectModel> getByNameAndAccount(String name, Account account) {
        return projectRepo.findByNameAndAccount(name, account);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<ProjectModel> getByAccount(Account account) {
        return projectRepo.findAllByAccount(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ProjectModel> getByAccountAsSet(Account account) {
        return getAsCollection(getByAccount(account), Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectModel> getByAccountAsList(Account account) {

        return getAsCollection(getByAccount(account), Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getNamesByAccount(Account account) {
        return getByAccount(account).map(ProjectModel::getName).collect(Collectors.toSet());
    }

    @Override
    public ProjectModel getNew() {
        return ProjectModel.builder().build();
    }


    @Override
    @Transactional
    public MetaData persistNewData(ProjectModel project, MetaData data) {
        project = merge(project);
        project.addMetaData(data);
        return metaDataService.save(data);
    }


    public static <T extends Collection<ProjectModel>> T getAsCollection(Stream<ProjectModel> stream,
                                                                         Collector<ProjectModel, ?, T> collector) {
        T projectsCollection = stream.collect(collector);
        stream.close();
        return projectsCollection;
    }


    public Optional<Algorithm> findAgorithmbyName(ProjectModel project, String algName) {
        return project.getAlgorithms().stream()
                .filter(prj -> prj.getName().equals(algName)).findAny();
    }

    public Optional<MetaData> findMetaDataByName(ProjectModel project, String dataName) {
        return project.getDataSources().stream()
                .filter(data -> data.getName().equals(dataName)).findAny();
    }

    private ProjectModel merge(ProjectModel project) {
        return em.merge(project);
    }


    @Transactional(readOnly = true)
    @Override
    public Stream<MetaData> getAllData(ProjectModel project) {
        project = merge(project);
        return project.getDataSources().stream();
    }


}
