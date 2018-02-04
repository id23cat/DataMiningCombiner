package evm.dmc.core.api.back.data;

import java.util.Properties;

public class DataStatistics extends DataInfo {
	private Properties statValues = new Properties();

	public DataStatistics(){
		super();
	}

	/**
	 * @return the statValues
	 */
	public Properties getStatValues() {
		return statValues;
	}

	/**
	 * @param statValues the statValues to set
	 */
	public void setStatValues(Properties statValues) {
		this.statValues = statValues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((statValues == null) ? 0 : statValues.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataStatistics other = (DataStatistics) obj;
		if (statValues == null) {
			if (other.statValues != null)
				return false;
		} else if (!statValues.equals(other.statValues))
			return false;
		return true;
	}
	
	
}
