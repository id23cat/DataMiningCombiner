package evm.dmc.model.service.impls;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.AlgorithmRepository;
import evm.dmc.model.service.AlgorithmService;
import evm.dmc.model.service.FrameworkFrontendService;
import evm.dmc.model.service.MetaDataService;
import evm.dmc.model.service.MethodService;
import evm.dmc.model.service.ProjectService;
import evm.dmc.webApi.dto.TreeNodeDTO;
import evm.dmc.webApi.exceptions.FunctionNotFoundException;
import evm.dmc.webApi.exceptions.MetaDataNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private FrameworkFrontendService frameworkService;

    @Autowired
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<Algorithm> getByProjectAndName(ProjectModel project, String name) {
        return algorithmRepository.findByProjectAndName(project, name);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Algorithm> getByProjectAndName(ProjectModel project, Set<String> names) {
//		return algorithmRepository.findByProjectAndNameIn(project, names).stream();
        return algorithmRepository.findByProjectAndNameIn(project, names).collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Algorithm> getForProject(ProjectModel project) {
        return algorithmRepository.findByProject(project).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void delete(Algorithm algorithm) {
        algorithmRepository.delete(algorithm);
    }

    @Override
    @Transactional
    public void delete(ProjectModel project, Set<String> algorithmNames) {
        algorithmRepository.deleteByProjectAndNameIn(project, algorithmNames);
    }

    @Override
    @Transactional
    public Algorithm addNew(ProjectModel project, Algorithm algorithm) {
        project = projectService.getOrSave(project);
        algorithm.setProject(project);
        project.getAlgorithms().add(algorithm);
        algorithm.setMethod(PatternMethod.patternBuilder()
                .dependentAlgorithm(algorithm)
                .build());
        return algorithmRepository.save(algorithm);
    }

    @Override
    @Transactional
    public Algorithm setDataSource(Algorithm algorithm, String datasetName) throws MetaDataNotFoundException {
        log.debug("Merge algorithm");
        algorithm = merge(algorithm);
        final ProjectModel project = algorithm.getProject();
        Optional<MetaData> optMeta = metaDataService.getByProjectAndName(project, datasetName);

        algorithm.setDataSource(optMeta.orElseThrow(() ->
                new MetaDataNotFoundException(
                        "No such dataset {" + datasetName +
                                "} for project " + project.getName())));
        return algorithm;
    }

    @Override
    public Optional<MetaData> getDataSource(Optional<Algorithm> optAlgorithm) {
        if (!optAlgorithm.isPresent()) {
            return Optional.ofNullable(null);
        }
        return optAlgorithm.map(Algorithm::getDataSource);
    }

    @Override
    @Transactional
    public Algorithm setAttributes(Algorithm algorithm, MetaData metaData) {
        algorithm = merge(algorithm);
        if (algorithm.getDataSource() == null ||
                !Objects.equals(algorithm.getDataSource().getName(), metaData.getName())) {
            setDataSource(algorithm, metaData.getName());
        }
        if (algorithm.getDataSource().getAttributes().equals(metaData.getAttributes()))
            return algorithm;
        algorithm.setSrcAttributes(metaData.getAttributes());
        return algorithm;
    }

    @Override
    @Transactional
    public Algorithm save(Algorithm algorithm) {
        return algorithmRepository.save(algorithm);
    }

    @Override
    @Transactional
    public List<FrameworkModel> getFrameworksList() {
        return frameworkService.getFrameworksList();
    }

    @Override
    @Transactional
    public List<TreeNodeDTO> getFrameworksAsTreeNodes() {
        return frameworkService.getFrameworksAsTreeNodes();
    }

    @Override
    @Transactional
    public Algorithm addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction)
            throws FunctionNotFoundException {
        if (algorithm.getMethod() == null || algorithm.getMethod().getSteps().isEmpty()) {
            return addMethod(algorithm, dtoFunction, 0);
        } else {
            Integer size = algorithm.getMethod().getSteps().size();
            return addMethod(algorithm, dtoFunction, size);
        }
    }

    @Override
    @Transactional
    public Algorithm addMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step)
            throws FunctionNotFoundException {
        algorithm = merge(algorithm);

        FWMethod method = getFWMethod(algorithm, dtoFunction);

        // get root pattermMethod from algorithm for convenience
        PatternMethod rootMethod = algorithm.getMethod();

//		// check out of bounds
//		step = step > rootMethod.getSteps().size() ? rootMethod.getSteps().size() : step;

        rootMethod.getSteps().add(step, method);

        return algorithm;
    }

    /* (non-Javadoc)
     * @see evm.dmc.web.service.MethodService#setMethod(evm.dmc.api.model.algorithm.Algorithm, evm.dmc.web.service.dto.TreeNodeDTO, java.lang.Integer)
     * @see https://docs.oracle.com/javase/8/docs/api/java/util/List.html#set-int-E-
     */
    @Override
//	@Transactional(propagation =  Propagation.REQUIRES_NEW)
    @Transactional
    public Algorithm setMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer step)
            throws FunctionNotFoundException, IndexOutOfBoundsException {
        algorithm = merge(algorithm);
        FWMethod method = getFWMethod(algorithm, dtoFunction);

        // get root pattermMethod from algorithm for convenience
        PatternMethod rootMethod = algorithm.getMethod();

        // add to the end
        log.debug("-== Size: {}", rootMethod.getSteps().size());
        log.debug("-== Strep: {}", step);
        if (rootMethod.getSteps().size() == step) {
            rootMethod.getSteps().add(step, method);
            log.debug("-== Steps: {}", rootMethod.getSteps());
        } else {
            rootMethod.getSteps().set(step, method);
        }
        return algorithm;
    }

    @Override
    @Transactional
    public Algorithm moveMethod(Algorithm algorithm, TreeNodeDTO dtoFunction, Integer toStep)
            throws FunctionNotFoundException {
        algorithm = merge(algorithm);
        PatternMethod method = getMethod(algorithm, dtoFunction.getId()).orElseThrow(
                fnNotFoundExc(PatternMethod.class.getName() + " step with ID = "
                        + dtoFunction.getId() + " not found"));
        algorithm.getMethod().getSteps().remove(method);

        PatternMethod rootMethod = algorithm.getMethod();
//		// check out of bounds
//		toStep = toStep > rootMethod.getSteps().size() ? rootMethod.getSteps().size() : toStep;

        rootMethod.getSteps().add(toStep, method);
        return algorithm;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatternMethod> getMethod(Algorithm algorithm, Long id) {
        if (id == null || algorithm.getMethod() == null)
            return Optional.empty();
        PatternMethod method = algorithm.getMethod();
        return method.getSteps().stream().filter(pm -> pm.getId().equals(id)).findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public PatternMethod getStep(Algorithm algorithm, Integer step) {
        algorithm = merge(algorithm);
        return algorithm.getMethod().getSteps().get(step);
    }


    private Algorithm merge(Algorithm algorithm) {
        return em.merge(algorithm);
    }

    private FWMethod getFWMethod(Algorithm algorithm, TreeNodeDTO dtoFunction)
            throws FunctionNotFoundException {
        // get function from framework repository
        Optional<FunctionModel> optFunction = frameworkService.getFunction(dtoFunction.getId());

        // create method from function
        FWMethod method = MethodService.functionToFWMethod(optFunction.orElseThrow(
                fnNotFoundExc("FWMethod with ID=" + dtoFunction.getId() + " not found")));

        // if algorithm doesn't have initialized root method yet -- create it
        if (algorithm.getMethod() == null)
            MethodService.algorithmCreatePatternMethod(algorithm);
        return method;
    }

    private Supplier<FunctionNotFoundException> fnNotFoundExc(String message) {
        return () -> new FunctionNotFoundException(message);
    }

}
