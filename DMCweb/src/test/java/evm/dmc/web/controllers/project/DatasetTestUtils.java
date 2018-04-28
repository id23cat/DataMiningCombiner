package evm.dmc.web.controllers.project;

import java.util.Map;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;

public class DatasetTestUtils {
	public static final String TEST_USER_NAME = "devel";
	public static final String TEST_PROJ_NAME = "proj0";
	public static final String TEST_DATA_NAME = "iris";
	public static final String TEST_data_telecom = "telecom";
	public static final String LINE0 = "5.1,3.5,1.4,0.2,Iris-setosa";
	public static final String LINE1 = "4.9,3.0,1.4,0.2,Iris-setosa";
	public static final String LINE2 = "4.7,3.2,1.3,0.2,Iris-setosa";
	public static final String LINE3 = "4.6,3.1,1.5,0.2,Iris-setosa";

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
				.line(LINE0)
				.line(LINE1)
				.line(LINE2)
				.line(LINE3)
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
}
