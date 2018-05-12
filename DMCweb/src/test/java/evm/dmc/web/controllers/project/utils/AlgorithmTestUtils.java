package evm.dmc.web.controllers.project.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.test.web.servlet.ResultActions;

import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.web.controllers.project.AlgorithmController;
import evm.dmc.web.controllers.project.DatasetController;
import evm.dmc.web.controllers.project.ProjectController;
import evm.dmc.web.service.dto.TreeNodeDTO;

public class AlgorithmTestUtils extends DatasetTestUtils{
	public static final String TEST_ALG0_NAME = "alg0";
	public static final String TEST_ALG1_NAME = "alg1";
	public static final String TEST_ALG2_NAME = "alg2";
	
	public static final Algorithm TEST_alg0 = Algorithm.builder()
			.name(TEST_ALG0_NAME)
			.project(TEST_session_project)
			.build();
	
	public static final Algorithm TEST_alg1 = Algorithm.builder()
			.name(TEST_ALG1_NAME)
			.project(TEST_session_project)
			.build();
	
	public static final Algorithm TEST_alg2 = Algorithm.builder()
			.name(TEST_ALG2_NAME)
			.project(TEST_session_project)
			.build();
	
	public static final List<TreeNodeDTO> functionsListDTO = Collections.singletonList(TreeNodeDTO.builder()
			.text("TestFramework")
			.id(0L)
			.nodes(Collections.singletonList(TreeNodeDTO.builder()
					.text("TestFunction")
					.id(1L)
					.build()))
			.build());
	
	public static final List<TreeNodeDTO> getFunctionsListDTO() {
		List<TreeNodeDTO> list = new ArrayList<>();
		list.add(TreeNodeDTO.builder()
			.text("TestFramework")
			.id(0L)
			.nodes(Collections.singletonList(TreeNodeDTO.builder()
					.text("TestFunction")
					.id(0L)
					.build()))
			.build());
		list.add(TreeNodeDTO.builder()
			.text("TestFramework")
			.id(1L)
			.nodes(Collections.singletonList(TreeNodeDTO.builder()
					.text("TestFunction1")
					.id(1L)
					.build()))
			.build());
		return list;
	}
	
	public static final List<Algorithm> TEST_algList = Arrays.asList(TEST_alg0, TEST_alg2, TEST_alg2);
	
	public static final ResultActions assertAlgorithmURLs(ResultActions resultActions, String projectName) 
			throws Exception {
		return resultActions.andExpect(model().attribute(AlgorithmController.MODEL_AlgBaseURL, 
				cookURL(AlgorithmController.BASE_URL, projectName)))
		.andExpect(model().attribute(DatasetController.MODEL_DataAttributesURL,
				cookURL(AlgorithmController.URL_ModifyAttributes, projectName)))
		.andExpect(model().attribute(AlgorithmController.MODEL_SelDataURL, 
				cookURL(AlgorithmController.URL_Select_DataSet, projectName)))
		.andExpect(model().attribute(AlgorithmController.MODEL_URL_DelAlgorithm, 
				cookURL(AlgorithmController.URL_Del_Algorithm, projectName)))
		.andExpect(model().attributeExists(ProjectController.MODEL_BackBean))
		.andExpect(model().attribute(AlgorithmController.MODEL_URL_SelectFunction, 
				cookURL(AlgorithmController.URL_Select_Function, projectName)))
		;
	}
	
	public static final ResultActions assertAlgorithmAttributesToModel(
			ResultActions resultActions,
			Optional<Algorithm> optAlg, 
			DataPreview preview
			) throws Exception {
		if(optAlg.isPresent()) {
			if(optAlg.get().getDataSource() != null) {
				return resultActions
						.andExpect(model().attribute(DatasetController.MODEL_MetaData, optAlg.get().getDataSource()))
						.andExpect(model().attribute(DatasetController.MODEL_Preview, preview))
						;
			} else {
				return resultActions
						.andExpect(model().attributeDoesNotExist(DatasetController.MODEL_MetaData))
						.andExpect(model().attributeDoesNotExist(DatasetController.MODEL_Preview))
						;
			}
			
			
		}
		return resultActions;
	}
}
