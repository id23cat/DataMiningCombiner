package evm.dmc.web.service.data;

import java.util.List;

import lombok.Data;

public interface DataPreview {
	@Data
	public class HeaderItem {
		private String name;
		private boolean checked = true;
		private float multiplicator = 1.0F;
		
		public HeaderItem(String name) {
			this.name = name;
		}
	}
	
	List<HeaderItem> getHeaderItems();
	String	getHeaderLine();
	
	List<String> getAllLines();
	String getDataLine(Integer line);
	List<String> getDataItems(Integer line);
	List<String> getDataItems(String line);
	Integer getLinesCount();
	
	DataPreview setDelimiter(String delimiter);

}
