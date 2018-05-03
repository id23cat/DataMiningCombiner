package evm.dmc.web.controllers.project.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.web.controllers.project.AlgorithmController;
import evm.dmc.web.controllers.project.DatasetController;

public class DatasetTestUtils {
	public static final String TEST_USER_NAME = "devel";
	public static final String TEST_PROJ_NAME = "proj0";
	public static final String TEST_DATA_NAME = "iris";
	public static final String TEST_data_telecom = "telecom";
	public static final String IRIS_LINE0 = "5.1,3.5,1.4,0.2,Iris-setosa";
	public static final String IRIS_LINE1 = "4.9,3.0,1.4,0.2,Iris-setosa";
	public static final String IRIS_LINE2 = "4.7,3.2,1.3,0.2,Iris-setosa";
	public static final String IRIS_LINE3 = "4.6,3.1,1.5,0.2,Iris-setosa";
	
	public static ProjectModel TEST_session_project = DatasetTestUtils.getProjectModel(DatasetTestUtils.getAccount());

	public static MetaData getMetaData(ProjectModel testSessionProject) {
		MetaData meta = MetaData.builder()
				.name(TEST_DATA_NAME)
				.project(testSessionProject)
				.build();
		Map<String,DataAttribute> map = meta.getAttributes();
		map.put("0", DataAttribute.builder().build());
		map.put("1", DataAttribute.builder().build());
		map.put("2", DataAttribute.builder().build());
		map.put("3", DataAttribute.builder().build());
		map.put("4", DataAttribute.builder().build());
		
		return meta;
	}
	
	public static DataPreview getDataPreview() {
		return DataPreview.builder()
				.metaDataId(1L)
				.header("0,1,2,3,4")
				.line(IRIS_LINE0)
				.line(IRIS_LINE1)
				.line(IRIS_LINE2)
				.line(IRIS_LINE3)
				.build();
		
	}
	
	public static DataStorageModel getDataStorageModel() {
		return DataStorageModel.builder().build();
	}
	
	public static Account getAccount() {
		return Account.builder().id(1L).userName(TEST_USER_NAME).build();
	}
	
	public static ProjectModel getProjectModel(Account account) {
		return ProjectModel.builder()
				.name(TEST_PROJ_NAME)
				.account(account)
				.build();
	}
	
	public static String cookURL(String URL, String projectName) {
		return UriComponentsBuilder.fromPath(URL)
		.buildAndExpand(projectName)
		.toUriString();
	}
	
	@SuppressWarnings("all")
	public static String cookURL(String URL, String ... args) {
		return UriComponentsBuilder.fromPath(URL)
		.buildAndExpand(args)
		.toUriString();
	}
	
	public static final ResultActions assertDatasetURLs(ResultActions resultActions, String projectName)
			throws Exception {
		return resultActions.andExpect(model().attribute(DatasetController.MODEL_DataBaseURL, 
					cookURL(DatasetController.BASE_URL, projectName)))
				.andExpect(model().attribute(DatasetController.MODEL_DataAttributesURL, 
						cookURL(DatasetController.URL_SetAttributes, projectName)))
				;
	}
	
	public static final ResultActions assertDatasetAttributesToModel(ResultActions resultActions, 
			List<MetaData> dataSets)  throws Exception {
		return resultActions.andExpect(model().attribute(DatasetController.MODEL_DataSets, dataSets))
				.andExpect(model().attributeExists(DatasetController.MODEL_DataSetProps))
				.andExpect(model().attributeExists(DatasetController.MODEL_SelectedNamesBean))
		;
	}
	
	public static final ResultActions assertDatasetAttributesToModel_WithMetaData(
			ResultActions resultActions, 
			Optional<MetaData> optMeta,
			DataPreview preview,
			List<MetaData> dataSets)  
					throws Exception {
		if(optMeta.isPresent()) {
			return resultActions
					.andExpect(model().attribute(DatasetController.MODEL_MetaData, optMeta.get()))
					.andExpect(model().attribute(DatasetController.MODEL_Preview, preview))
					.andExpect(model().attributeExists(DatasetController.MODEL_DataSetProps))
			;
		} else {
			return assertDatasetAttributesToModel(resultActions, dataSets);
		}
	}
}
