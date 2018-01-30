package evm.dmc.web.service.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public class DataPreviewImpl implements DataPreview {
	String header;
	List<String> dataLines = new ArrayList<>();
	String delimiters = ",;\t|";


	public DataPreviewImpl(List<String> lines) {
		this.header = lines.get(0);
		this.dataLines.addAll(lines.stream().skip(1).collect(Collectors.toList()));
	}
	
	
	@Override
	public List<HeaderItem> getHeaderItems() {
		
		return stringToStreamOfWords(header,delimiters).map(mapToHeaderItems).collect(Collectors.toList());
	}

	@Override
	public String getHeaderLine() {
		return header;
	}

	@Override
	public List<String> getAllLines() {
		return Collections.unmodifiableList(dataLines); 
	}
	
	@Override
	public String getDataLine(Integer line) {
		return dataLines.get(line);
	}
	
	
	@Override
	public Integer getLinesCount() {
		return dataLines.size();
	}
	
	public void addDataLine(String line) {
		dataLines.add(line);
	}


	@Override
	public List<String> getDataItems(Integer line) {
		return stringToStreamOfWords(dataLines.get(line),delimiters).collect(Collectors.toList());
	}

	@Override
	public DataPreview setDelimiter(String delimiter) {
		this.delimiters = delimiter;
		return this;
	}

	
	private static final Function<String, HeaderItem> mapToHeaderItems = (word) -> {
		return new HeaderItem(word);
	};
	
	private static final Stream<String> stringToStreamOfWords(String line, String delimiters) {
		return Arrays.stream(StringUtils.tokenizeToStringArray(line, delimiters));
	}

	

}
