package evm.dmc.web.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;

public interface DataPreviewService {
	DataPreview getByMetaDataId(Long metaDataId);
	DataPreview getForMetaData(MetaData mdata);
	
	DataPreview save(DataPreview preview);
	
	static Stream<String> streamLine(String line, String delimiters) {
		return Arrays.stream(StringUtils.tokenizeToStringArray(line, delimiters));
	}
	
	static List<String> listLine(String line, String delimiters) {
		return Arrays.asList(StringUtils.tokenizeToStringArray(line, delimiters));
	}
	
	static DataPreview newDataPreview(String header, List<String> data) {
		DataPreview preview = new DataPreview();
		preview.setHeader(header);
		preview.setHeader(header);
		return preview;
	}

	static DataPreview newDataPreview(String header, List<String> data, String delimiter) {
		DataPreview preview = newDataPreview(header, data);
		preview.setDelimiter(delimiter);
		return preview;
	}

	static DataPreview newDataPreview(String header, List<String> data, MetaData meta) {
		DataPreview preview = DataPreviewService.newDataPreview(header, data);
		preview.setMetaDataId(meta.getId());
		return preview;
	}

}
