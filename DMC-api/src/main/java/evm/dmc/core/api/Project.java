package evm.dmc.core.api;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.ProjectModel;

public interface Project {

//	static Project CreateProject() {
//		return new SimplestProjectImpl();
//	}

	Project setModel(ProjectModel model);

	ProjectModel getModel();

	Algorithm createAlgorithm();

	Algorithm createAlgorithm(AlgorithmModel algModel);

	Algorithm getAlgorithm();

}
