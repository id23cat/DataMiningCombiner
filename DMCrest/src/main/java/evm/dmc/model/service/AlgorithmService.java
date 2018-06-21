package evm.dmc.model.service;


import java.util.List;
import java.util.Optional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.webApi.dto.TreeNodeDTO;
import evm.dmc.webApi.exceptions.MetaDataNotFoundException;

public interface AlgorithmService extends 
	EnityGetterService<Algorithm>, 
	EntityModifierService<Algorithm>, 
	MethodService {
	
	Algorithm addNew(ProjectModel project, Algorithm algorithm);
	Algorithm setDataSource(Algorithm algorithm, String datasetName) throws MetaDataNotFoundException;
//	MetaData getDataSource(Algorithm algorithm);
	Optional<MetaData> getDataSource(Optional<Algorithm> optAlgorithm);
	
	Algorithm setAttributes(Algorithm algorithm, MetaData metaData);
	
	
	
	List<FrameworkModel> getFrameworksList();
	List<TreeNodeDTO> getFrameworksAsTreeNodes();
	
	static Algorithm getNewAlgorithm(ProjectModel project) {
		return Algorithm.builder().project(project).build();
	}
}
