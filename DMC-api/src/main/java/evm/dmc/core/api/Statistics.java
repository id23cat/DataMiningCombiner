package evm.dmc.core.api;

import java.util.Map;
import java.util.Set;

public class Statistics {
	AttributeType type;
	String attrName;
	Map<Object, Integer> mapValuesCount;
	double max = 0;
	double min = 0;
	int count = 0;
	int bins = 30;
	Set<String> elements;

	public Statistics(AttributeType type, String name) {
		this.type = type;
		this.attrName = name;
	}

	public AttributeType getType() {
		return type;
	}

	public String getName() {
		return attrName;
	}

	/**
	 * @return the mapValCount
	 */
	public Map<Object, Integer> getMapValuesCount() {
		return mapValuesCount;
	}

	/**
	 * @param mapValCount
	 *            the mapValCount to set
	 */
	public void setMapValuesCount(Map<Object, Integer> mapValCount) {
		this.mapValuesCount = mapValCount;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the count
	 */
	public int getBins() {
		return bins;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setBins(int bins) {
		this.bins = bins;
	}

	/**
	 * @return the elements
	 */
	public Set<String> getElements() {
		return elements;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public void setElements(Set<String> elements) {
		this.elements = elements;
	}

}
