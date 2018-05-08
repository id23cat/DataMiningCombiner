package evm.dmc.web.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;

public interface FrameworkFrontendService {
	List<FrameworkModel> getFrameworksList();
	Optional<FrameworkModel> getFramework(String name);
	Optional<FunctionModel> getFunction(String name);
	Stream<FunctionModel> findFunctionByWord(String word);
	Stream<FunctionModel> getAllFunctions();
	Stream<FunctionSrcModel> getDataLoaders();
	Stream<FunctionDstModel> getDataSavers();
}
