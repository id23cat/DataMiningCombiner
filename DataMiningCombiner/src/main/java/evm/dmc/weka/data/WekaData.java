package evm.dmc.weka.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import evm.dmc.core.DataFactory;
import evm.dmc.core.api.AttributeType;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;
import evm.dmc.core.api.back.HasMultiAttributes;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.core.api.exceptions.IndexOutOfRange;
import evm.dmc.core.data.HasMultiInstaces;
import evm.dmc.core.data.InMemoryData;
import evm.dmc.weka.WekaFW;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

/**
 * Supported loading from all supported by weka files. Now supports saving only
 * * to SCV files Default separator
 * 
 * @see WEKA CSCSaver documentation
 *      http://weka.sourceforge.net/doc.dev/weka/core/converters/CSVSaver.html
 * 
 * @author id23cat
 */
@Service("Weka_Instances")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
// TODO: too complicated class !!!
public class WekaData extends InMemoryData<Instances> implements Cloneable, HasMultiAttributes, HasMultiInstaces {

	// used for generating cross-validation sets
	private static int FOLDS = 4;

	private DataFactory dataFactory;

	public WekaData(@Autowired @WekaFW DataFactory df) {
		this.dataFactory = df;
	}

	@Override
	public WekaData clone() {
		WekaData newData = (WekaData) dataFactory.getData(WekaData.class);
		newData.data = new Instances(this.data);
		return newData;

	}

	public WekaData copyObject() throws CloneNotSupportedException {
		WekaData data = (WekaData) dataFactory.getData(WekaData.class);
		data.setData(new Instances(this.getData()));
		return data;

	}

	@Override
	public Enumeration<Object> enumerateValues(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		if (!isNominal(column))
			return null;
		return data.attribute(column).enumerateValues();
	}

	@Override
	public Enumeration<Object> enumerateValues(String name) throws IndexOutOfRange {
		return enumerateValues(getIndexByName(name));
	}

	public double[] getAllValuesAsDoubleAt(int index) {
		Instances inst = super.getData();
		double[] values = inst.attributeToDoubleArray(index);
		return values;

	}

	public double[] getAllValuesAsDoubleAt(String attrName) {
		return this.getAllValuesAsDoubleAt(getIndexByName(attrName));
	}

	@Override
	public WekaData getAttribute(int index) throws IndexOutOfRange {
		WekaData data;
		try {
			data = this.copyObject();
		} catch (CloneNotSupportedException e) {
			throw new DataOperationException(e);
		}

		Instances inst = data.getData();
		Attribute attrSaveFromDelete = inst.attribute(index);
		int attrsCount = inst.numAttributes();
		int curIndex = 0;
		for (int i = 0; i < attrsCount; i++) {
			if (!inst.attribute(curIndex).equals(attrSaveFromDelete))
				inst.deleteAttributeAt(curIndex);
			else
				curIndex++;
		}

		return data;
	}

	@Override
	public WekaData getAttribute(String name) throws IndexOutOfRange {
		return getAttribute(getIndexByName(name));
	}

	@Override
	public String getAttributeName(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		return data.attribute(column).name();
	}

	@Override
	public Data<Instances> getAttributes(int... indexes) throws IndexOutOfRange {
		WekaData data;
		try {
			data = this.copyObject();
		} catch (CloneNotSupportedException e) {
			throw new DataOperationException(e);
		}

		Instances inst = data.getData();
		Set<Attribute> attrSaveFromDelSet = new HashSet<>();

		try {
			for (int index : indexes) {
				Attribute attrSaveFromDelete = inst.attribute(index);
				attrSaveFromDelSet.add(attrSaveFromDelete);
			}
		} catch (Exception exc) {
			throw new IndexOutOfRange(exc);
		}

		int attrsCount = inst.numAttributes();
		int curIndex = 0;
		for (int i = 0; i < attrsCount; i++) {
			if (!attrSaveFromDelSet.contains(inst.attribute(curIndex)))
				inst.deleteAttributeAt(curIndex);
			else
				curIndex++;
		}

		return data;
	}

	@Override
	public int getAttributesCount() {
		return data.numAttributes();
	}

	@Override
	public Statistics getAttributeStatistics(int column) throws IndexOutOfRange {

		Statistics stat = new Statistics(getType(column), getAttributeName(column));
		AttributeStats wekaStat = data.attributeStats(column);
		if (isNumeric(column)) {
			stat.setCount(wekaStat.intCount);
			stat.setMax(wekaStat.numericStats.max);
			stat.setMin(wekaStat.numericStats.min);
			// stat.setBins(wekaStat.intCount);
		} else {
			stat.setCount(wekaStat.intCount);
			ArrayList<?> list = Collections.list(data.attribute(column).enumerateValues());
			stat.setElements(new HashSet<String>((Collection<? extends String>) list));
		}

		Map map = countElements(column, stat);
		stat.setMapValuesCount(map);

		return stat;
	}

	@Override
	public Statistics getAttributeStatistics(int column, int bins) throws IndexOutOfRange {
		Statistics stat = new Statistics(getType(column), getAttributeName(column));
		AttributeStats wekaStat = data.attributeStats(column);
		if (isNumeric(column)) {
			stat.setCount(wekaStat.intCount);
			stat.setMax(wekaStat.numericStats.max);
			stat.setMin(wekaStat.numericStats.min);
			stat.setBins(bins);
			Map map = countElements(column, stat);
			stat.setMapValuesCount(map);
		}
		return stat;
	}

	@Override
	public int getElementsCount() {
		return getInstancesCount();
	}

	@Override
	public int getIndexByName(String name) throws IndexOutOfRange {
		if (data.attribute(name) == null) {
			throw new IndexOutOfRange(exceptionMessage("Wrong attribute name: ", name));
		}
		return data.attribute(name).index();
	}

	@Override
	public WekaData getInstance(int index) throws IndexOutOfRange {
		WekaData newData = (WekaData) dataFactory.getData(WekaData.class);
		try {
			newData.data = new Instances(this.data, index, 1);
		} catch (IllegalArgumentException exc) {
			throw new IndexOutOfRange(exc);
		}
		return newData;
	}

	@Override
	public int getInstancesCount() {
		return data.numInstances();
	}

	@Override
	public WekaData getSubset(int from, int to) throws IndexOutOfRange {
		WekaData newData = (WekaData) dataFactory.getData(WekaData.class);
		try {
			newData.data = new Instances(this.data, from, to - from + 1);
		} catch (IllegalArgumentException exc) {
			throw new IndexOutOfRange(exc);
		}
		return newData;
	}

	@Override
	public String getTitle(int index) {
		return getAttributeName(index);
	}

	@Override
	public Data<?>[] getTrainTest(int numFold) {
		if (numFold > 3)
			numFold = 3;
		if (numFold < 0)
			numFold = 0;

		// int seed = 514;
		// Random rand = new Random(seed);
		WekaData train = (WekaData) dataFactory.getData(WekaData.class);
		WekaData test = (WekaData) dataFactory.getData(WekaData.class);

		train.setData(this.data.trainCV(FOLDS, numFold));
		test.setData(this.data.testCV(FOLDS, numFold));

		WekaData[] traintest = new WekaData[] { train, test };
		return traintest;
	}

	@Override
	public double getValue(int row, int column) throws IndexOutOfRange {
		if (!checkRowIndex(row) || !checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", row, column));
		}
		return this.data.get(row).value(column);
	}

	@Override
	public String getValueAsString(int row, int column) throws IndexOutOfRange {
		if (!checkRowIndex(row) || !checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", row, column));
		}
		Attribute attr = this.data.get(row).attribute(column);
		if (attr.isNominal() || attr.isString() || attr.isDate() || attr.isRelationValued())
			return this.data.get(row).stringValue(column);
		return String.valueOf(this.data.get(row).value(column));
	}

	@Override
	public boolean isMultiAttribute() {
		return true;
	}

	@Override
	public boolean isDate(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		return this.data.attribute(column).isDate();
	}

	// @Override
	// public void toDate(int column) throws IndexOutOfRange,
	// DataOperationException {
	// Filter filter;
	// switch (getType(column)) {
	// case STRING:
	// // TODO
	// throw new DataOperationException("Convertion not implemented: Date to
	// Numeric");
	// case NOMINAL:
	// // TODO
	// throw new DataOperationException("Convertion not implemented: Date to
	// Numeric");
	// case NUMERIC:
	// // TODO
	// throw new DataOperationException("Convertion not implemented: Date to
	// Numeric");
	// default:
	// return;
	// }
	// // try {
	// // filter.setInputFormat(data);
	// // } catch (Exception e) {
	// // throw new DataOperationException(e);
	// // }
	// // applyFilter(filter);
	// }

	@Override
	public boolean isNominal(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		return this.data.attribute(column).isNominal() || this.data.attribute(column).isString();
	}

	@Override
	public void toNominal(int column) throws IndexOutOfRange, DataOperationException {
		Filter filter;
		switch (getType(column)) {
		case STRING:
			StringToNominal str2nom = new StringToNominal();
			str2nom.setAttributeRange(String.valueOf(column + 1));
			filter = str2nom;
			break;
		case DATE:
		case NUMERIC:
			NumericToNominal num2nom = new NumericToNominal();
			num2nom.setAttributeIndices(String.valueOf(column + 1));
			filter = num2nom;
			break;
		default:
			return;
		}
		try {
			filter.setInputFormat(data);
		} catch (Exception e) {
			throw new DataOperationException(e);
		}
		applyFilter(filter);

	}

	@Override
	public boolean isNumeric(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		return this.data.attribute(column).isNumeric();
	}

	// @Override
	// public void toNumeric(int column) throws IndexOutOfRange,
	// DataOperationException {
	// Filter filter;
	// switch (getType(column)) {
	// case DATE:
	// // TODO
	// throw new DataOperationException("Convertion not implemented: Date to
	// Numeric");
	// case STRING:
	// // TODO
	// throw new DataOperationException("Convertion not implemented: String to
	// Numeric");
	// case NOMINAL:
	// throw new DataOperationException("Convertion not implemented: Nominal to
	// Numeric");
	// default:
	// return;
	// }
	// // try {
	// // filter.setInputFormat(data);
	// // } catch (Exception e) {
	// // throw new DataOperationException(e);
	// // }
	// // applyFilter(filter);
	// }

	@Override
	public boolean isString(int column) throws IndexOutOfRange {
		if (!checkColIndex(column)) {
			throw new IndexOutOfRange(exceptionMessage("Wrong index: ", column));
		}
		return this.data.attribute(column).isString();
	}

	@Override
	public double[] plot(int index) {
		return this.getAllValuesAsDoubleAt(index);
	}

	@Override
	public String getAllAsString() {
		StringBuilder strBuf = new StringBuilder();
		int cols = this.getAttributesCount();
		int rows = this.getElementsCount();
		for (int j = 0; j < cols; j++) {
			strBuf.append(this.getAttributeName(j) + "(");
			strBuf.append(this.getType(j).name() + ")\t");
		}
		strBuf.append(System.lineSeparator());

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				strBuf.append(this.getValue(i, j) + " (");
				strBuf.append(this.getValueAsString(i, j) + ")\t");
			}
			strBuf.append(System.lineSeparator());
		}
		return strBuf.toString();
	}

	public void printDebug() {
		System.out.println(getAllAsString());
	}

	private AttributeType getType(int index) {
		AttributeType type;
		if (isDate(index))
			type = AttributeType.DATE;
		else if (isString(index))
			type = AttributeType.STRING;
		else if (isNumeric(index))
			type = AttributeType.NUMERIC;
		else
			type = AttributeType.NOMINAL;
		return type;
	}

	private void applyFilter(Filter filter) throws DataOperationException {
		try {
			data = Filter.useFilter(data, filter);
		} catch (Exception e) {
			throw new DataOperationException(e);
		}
	}

	private boolean checkColIndex(int index) throws IndexOutOfRange {
		if (index < 0 || index >= getAttributesCount())
			return false;
		return true;
	}

	private boolean checkRowIndex(int index) throws IndexOutOfRange {
		if (index < 0 || index >= getInstancesCount())
			return false;
		return true;
	}

	private Map<Object, Integer> countElements(int index, Statistics stat) {
		Map<Object, Integer> mapRet;
		switch (getType(index)) {
		case DATE:
		case NOMINAL:
		case STRING:
			Map<String, Integer> mapS = new HashMap<>();
			countAsStrings(index, mapS);

			mapRet = new HashMap<Object, Integer>(mapS);
			countAsStrings(index, mapS);
			break;
		case NUMERIC:
			Map<Double, Integer> mapD = new HashMap<>();
			countAsDoubles(index, mapD, stat);

			mapRet = new HashMap<Object, Integer>(mapD);
			countAsDoubles(index, mapD, stat);
			break;
		default:
			mapRet = null;
		}

		return mapRet;

	}

	private void countAsStrings(int index, Map<String, Integer> map) {
		for (int i = 0; i < getInstancesCount(); i++) {
			String key = getValueAsString(i, index);
			if (map.containsKey(key)) {
				int count = map.get(key);
				map.put(key, count + 1);
			} else {
				map.put(key, 1);
			}
		}
	}

	private void countAsDoubles(int index, Map<Double, Integer> map, Statistics stat) {
		double min = stat.getMin();
		double max = stat.getMax();
		int bins = stat.getBins();
		double[] boundaries = new double[bins + 1];
		int[] counts = new int[bins + 1];
		double step = (max - min) / bins;
		for (int i = 0; i < bins; i++) {
			boundaries[i] = min;
			min += step;
		}
		boundaries[bins] = max;
		for (int i = 0; i < getInstancesCount(); i++) {
			double key = getValue(i, index);
			int counter = Arrays.binarySearch(boundaries, key);
			try {

				if (counter >= 0)
					counts[counter]++;
				else
					counts[-counter - 1]++;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(step);
				System.err.println(Arrays.toString(boundaries));
				System.err.println(i + ", " + key + ", " + counter);
				e.printStackTrace();
				throw e;
			}
		}
		for (int i = 0; i < bins; i++) {
			map.put(boundaries[i], counts[i]);
		}
	}

	private String exceptionMessage(String msg, int... idx) {
		StringBuilder strb = new StringBuilder(msg);
		int i = 0;
		while (i < idx.length) {
			strb.append(idx[i]);
			if (i < idx.length - 1)
				strb.append(", ");
			i++;
		}

		return strb.toString();

	}

	private String exceptionMessage(String msg, String name) {
		StringBuilder strb = new StringBuilder(msg);
		strb.append(name);

		return strb.toString();
	}

	@Override
	public DataModel getDataModel() throws DataOperationException {
		return getDataModel(DataModel.DEFAULT_ROWS_COUNT);
	}

	@Override
	public DataModel getDataModel(Integer previewRowsCount) throws DataOperationException {
		if(data == null)
			throw new DataOperationException("Empty object: Data does not exists");
		
		DataModel model = new DataModel();
		int atrCount = getAttributesCount();
		int rows = getInstancesCount();
		previewRowsCount = previewRowsCount <= rows ? previewRowsCount : rows;
		model.setRowsCount(previewRowsCount);
		String[][] preview = new String[previewRowsCount][atrCount];
		for (int i = 0; i < atrCount; i++) {
			model.getTitleTypeMap().put(getTitle(i), getType(i).name());
			for (int k = 0; k < previewRowsCount; k++) {
				preview[k][i] = getValueAsString(k, i);
			}
		}

		model.setPreview(preview);
		return model;
	}

}
