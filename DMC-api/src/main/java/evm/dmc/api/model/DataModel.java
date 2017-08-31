package evm.dmc.api.model;

import java.util.Arrays;

public class DataModel {
	// TODO: using JSON
	
	private String[] titles;
	private String[] types;
	private Integer rowsCount;
	private String[][] preview;
	
	public DataModel() {}

	/**
	 * @return the titles
	 */
	public String[] getTitles() {
		return titles;
	}

	/**
	 * @param titles the titles to set
	 */
	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	/**
	 * @return the types
	 */
	public String[] getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String[] types) {
		this.types = types;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(preview);
		result = prime * result + ((rowsCount == null) ? 0 : rowsCount.hashCode());
		result = prime * result + Arrays.hashCode(titles);
		result = prime * result + Arrays.hashCode(types);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModel other = (DataModel) obj;
		if (!Arrays.deepEquals(preview, other.preview))
			return false;
		if (rowsCount == null) {
			if (other.rowsCount != null)
				return false;
		} else if (!rowsCount.equals(other.rowsCount))
			return false;
		if (!Arrays.equals(titles, other.titles))
			return false;
		if (!Arrays.equals(types, other.types))
			return false;
		return true;
	}

}
