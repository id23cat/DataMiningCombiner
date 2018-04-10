package evm.dmc.web.service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.core.api.back.data.DataSrcDstType;

public interface MetaDataService extends EnityGetter<MetaData>, EntityModifier<MetaData>{

	MetaData getMetaData(ProjectModel project, Path fullFilePath,
    		Path baseDir, DataSrcDstType type, String delimiter, DataSetProperties datasetProperities);
	
	Optional<DataPreview> getPreview(MetaData meta);
	
	MetaData persistMetadata(MetaData meta, ProjectModel project);
	
	DataPreview createPreview(MetaData meta, List<String> previewLines);
	
	Map<MetaData, DataPreview> persistPreview(MetaData meta, DataPreview preview);
	Map<MetaData, DataPreview> persistPreview(Map<MetaData, DataPreview> map);
	
	MetaData persistAttributes(MetaData meta, List<DataAttribute> attributes);
	
	MetaData generateAndPersistAttributes(MetaData meta, DataPreview preview);
	
	List<DataAttribute> getDataAttributes(DataPreview preview);
	
	public static DataStorageModel newDataStorageModel(Path baseDir, Path fullFilePath,
			DataSrcDstType type, String delimiter, boolean hasHeader) {
		DataStorageModel storageDesc = new DataStorageModel();

		storageDesc.setStorageType(type);
		storageDesc.setUri(fullFilePath.toUri(), baseDir.toUri().toString());
		storageDesc.setDelimiter(delimiter);
		storageDesc.setHasHeader(hasHeader);

		return storageDesc;
	}

	public static MetaData newMetaData(String name, String description, DataStorageModel dataStore) {
		MetaData meta = new MetaData();
		meta.setName(name);
		meta.setDescription(description);
		meta.setStorage(dataStore);

		return meta;
	}
	
	public static Stream<String> streamLine(String line, String delimiters) {
		return Arrays.stream(StringUtils.tokenizeToStringArray(line, delimiters));
	}
	
	public static List<String> listLine(String line, String delimiters) {
		return Arrays.asList(StringUtils.tokenizeToStringArray(line, delimiters));
	}
	
	public static String getActiveDelimiters(String testLine, String delimiters) {
		int size = delimiters.length();
		StringJoiner joiner = new StringJoiner("");
		testLine = testLine.trim();
		for(int i=0; i<size; i++) {
			String symbol = String.valueOf(delimiters.charAt(i));
			if(StringUtils.tokenizeToStringArray(testLine, symbol).length > 1)
				joiner.add(symbol);
		}
		return joiner.toString();
	}
}
