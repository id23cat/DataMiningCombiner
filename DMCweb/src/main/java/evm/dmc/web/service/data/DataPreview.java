package evm.dmc.web.service.data;

import java.util.ArrayList;
import java.util.List;

import evm.dmc.core.api.AttributeType;
import lombok.Data;

public interface DataPreview {
	@Data
	public class ItemsList {
//		private HeaderItem[] items= null;
		private List<HeaderItem> items= new ArrayList<>();
		public ItemsList() {}
		public ItemsList(List<HeaderItem> items) {
			this.items = items;
		}
	}
	@Data
	public class HeaderItem {
		private String name;
		private boolean checked = true;
		private float multiplicator = 1.0F;
		private AttributeType type = AttributeType.STRING;
		
		public HeaderItem(String name) {
			this.name = name;
		}
		public HeaderItem() {}
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
