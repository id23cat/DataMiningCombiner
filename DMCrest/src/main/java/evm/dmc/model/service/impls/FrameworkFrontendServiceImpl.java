package evm.dmc.model.service.impls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.api.model.FunctionType;
import evm.dmc.model.repositories.FrameworkFrontendRepository;
import evm.dmc.model.repositories.FunctionFrontendRepository;
import evm.dmc.model.service.FrameworkFrontendService;
import evm.dmc.webApi.dto.TreeNodeDTO;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FrameworkFrontendServiceImpl implements FrameworkFrontendService {

    FrameworkFrontendRepository frameworkRepo;
    FunctionFrontendRepository functionRepo;

    public FrameworkFrontendServiceImpl(@Autowired FrameworkFrontendRepository frameworkRepo,
                                        @Autowired FunctionFrontendRepository functionRepo) {
        this.frameworkRepo = frameworkRepo;
        this.functionRepo = functionRepo;
    }

    @Override
    @Transactional
    public List<FrameworkModel> getFrameworksList() {
        return frameworkRepo.findAll(Sort.by("name"));
    }

    @Override
    @Transactional
    public List<TreeNodeDTO> getFrameworksAsTreeNodes() {
        // Recursively convert FrameworkModel->Set<Function> to TreeNodeDTO
        return frameworkRepo.streamAll()
                .parallel()
                .map((fm) -> {
                    return TreeNodeDTO
                            .builder()
                            .id(fm.getId())
                            .text(fm.getName())
                            .nodes(fm.getFunctions()
                                    .stream()
                                    .parallel()
                                    .map((func) -> {
                                        return TreeNodeDTO
                                                .builder()
                                                .id(func.getId())
                                                .text(func.getName())
                                                .tooltip(func.getDescription())
                                                .selectable(true)
                                                .build();
                                    })
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FrameworkModel> getFramework(String name) {
        return frameworkRepo.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FunctionModel> getFunction(Long id) {
        return functionRepo.findById(id);
    }

    @Override
    public Optional<FunctionModel> getFunction(String name) {
        return functionRepo.findByName(name);
    }

    @Override
    public Stream<FunctionModel> findFunctionByWord(String word) {
        return functionRepo.findByNameIgnoreCaseContaining(word);
    }

    @Override
    public Stream<FunctionModel> getAllFunctions() {
        return functionRepo.StreamAllFunctions();
    }

    @Override
    public Stream<FunctionSrcModel> getDataLoaders() {
        return functionRepo.findByType(FunctionType.CSV_DATASOURCE);
    }

    @Override
    public Stream<FunctionDstModel> getDataSavers() {
        return functionRepo.findByType(FunctionType.CSV_DATADESTINATION);
    }

}
