package evm.dmc.core.api.back.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class DataModel {
	// TODO: using JSON
	public static final Integer DEFAULT_ROWS_COUNT = 10;
	private Map<String, String> titleTypeMap = new HashMap<>();
	private Integer rowsCount = DEFAULT_ROWS_COUNT;
	private String[][] preview;
	
	public DataModel() {}

	/**
	 * @return the map {@code Map<title, type>}
	 */
	public Map<String, String> getTitleTypeMap() {
		return titleTypeMap;
	}

	/**
	 * @param titleTypesMap the titleTypesMap to set
	 */
	public void setTitleTypeMap(Map<String, String> titleTypesMap) {
		this.titleTypeMap = titleTypesMap;
	}

	/**
	 * @return the rowsCount
	 */
	public Integer getRowsCount() {
		return rowsCount;
	}

	/**
	 * @param rowsCount the rowsCount to set
	 */
	public void setRowsCount(Integer rowsCount) {
		this.rowsCount = rowsCount;
	}

	/**
	 * @return the preview
	 */
	public String[][] getPreview() {
		return preview;
	}

	/**
	 * @param preview the preview to set
	 */
	public void setPreview(String[][] preview) {
		this.preview = preview;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DataModel)) {
			return false;
		}
		DataModel castOther = (DataModel) other;
		return Objects.equals(titleTypeMap, castOther.titleTypeMap) && Objects.equals(rowsCount, castOther.rowsCount)
				&& Objects.equals(preview, castOther.preview);
	}

	@Override
	public int hashCode() {
		return Objects.hash(titleTypeMap, rowsCount, preview);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("titleTypeMap", titleTypeMap)
				.append("rowsCount", rowsCount).append("preview", preview).toString();
	}

//	@Override
//	public String toString() {
//		return Objects.toString(titleTypeMap)
//	}
	
	

}
