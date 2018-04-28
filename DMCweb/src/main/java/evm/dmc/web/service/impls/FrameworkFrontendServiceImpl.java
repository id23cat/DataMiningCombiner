package evm.dmc.web.service.impls;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.api.model.FunctionType;
import evm.dmc.model.repositories.FrameworkFrontendRepository;
import evm.dmc.model.repositories.FunctionFrontendRepository;
import evm.dmc.web.service.FrameworkFrontendService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FrameworkFrontendServiceImpl implements FrameworkFrontendService {
	
	FrameworkFrontendRepository frameworkRepo;
	FunctionFrontendRepository functionRepo;

	public FrameworkFrontendServiceImpl(@Autowired	FrameworkFrontendRepository frameworkRepo,
			@Autowired FunctionFrontendRepository functionRepo) {
		this.frameworkRepo = frameworkRepo;
		this.functionRepo = functionRepo;
	}

	@Override
	public Optional<FrameworkModel> getFramework(String name) {
		return frameworkRepo.findByName(name);
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
