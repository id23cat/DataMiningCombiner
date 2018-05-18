package evm.dmc.web.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.web.service.dto.TreeNodeDTO;

public interface FrameworkFrontendService {
	List<FrameworkModel> getFrameworksList();
	List<TreeNodeDTO> getFrameworksAsTreeNodes();
	Optional<FrameworkModel> getFramework(String name);
	Optional<FunctionModel> getFunction(Long id);
	Optional<FunctionModel> getFunction(String name);
	Stream<FunctionModel> findFunctionByWord(String word);
	Stream<FunctionModel> getAllFunctions();
	Stream<FunctionSrcModel> getDataLoaders();
	Stream<FunctionDstModel> getDataSavers();
	
	static Optional<TreeNodeDTO> functionToDTO(Optional<FunctionModel> function) {
		if(!function.isPresent())
			return Optional.ofNullable(null);
		return Optional.of(TreeNodeDTO.builder()
			.id(function.get().getId())
			.text(function.get().getName())
			.tooltip(function.get().getDescription())
			.build()
			);
	}
}
