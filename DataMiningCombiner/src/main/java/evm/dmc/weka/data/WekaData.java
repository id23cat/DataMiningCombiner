package evm.dmc.weka.data;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import evm.dmc.core.DataFactory;
import evm.dmc.core.chart.Plottable;
import evm.dmc.core.data.Data;
import evm.dmc.core.data.FromFileLoadable;
import evm.dmc.core.data.HasMultiAttributes;
import evm.dmc.core.data.InMemoryData;
import evm.dmc.core.data.MultyInstace;
import evm.dmc.core.data.ToFileStorable;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.exceptions.DataOperationError;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.AbstractSaver;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Supported loading from all supported by weka files.
 * Now supports saving only * to SCV files Default separator
 * 
 * @see WEKA CSCSaver documentation
 *      http://weka.sourceforge.net/doc.dev/weka/core/converters/CSVSaver.html
 * 
 * @author id23cat
 */
@Service("Weka_Instances")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WekaData extends InMemoryData<Instances>
		implements FromFileLoadable, ToFileStorable, HasMultiAttributes, MultyInstace, Plottable {

	private DataFactory dataFactory;

	// used for generating cross-validation sets
	private static int FOLDS = 4;

	public WekaData(@Autowired @WekaFW DataFactory df) {
		this.dataFactory = df;
	}

	@Override
	public Data load(String fileName) throws Exception {
		Instances data = DataSource.read(fileName);
		// MLUtils.prepareData(data);
		super.setData(data);
		return this;
	}

	@Override
	public void store(String fileName) throws Exception {
		File file = new File(fileName);
		AbstractSaver saver = new CSVSaver();
		saver.setFile(file);
		saver.setInstances(super.getData());
		saver.writeBatch();

	}

	public double[] getAllValuesAsDoubleAt(int index) {
		Instances inst = super.getData();
		// // if (!inst.attribute(index).isNumeric())
		// // return null;
		// int length = inst.numInstances();
		// double[] values = new double[length];
		// for (int i = 0; i < length; i++) {
		// values[i] = inst.instance(i).value(index);
		// }
		double[] values = inst.attributeToDoubleArray(index);
		return values;

	}

	public double[] getAllValuesAsDoubleAt(String attrName) {
		return this.getAllValuesAsDoubleAt(super.getData().attribute(attrName).index());
	}

	@Override
	public Data<Instances> getAttribute(int index) {
		WekaData data;
		try {
			data = this.copyObject();
		} catch (CloneNotSupportedException e) {
			throw new DataOperationError(e);
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
	public Data<Instances> getAttributes(int... indexes) {
		WekaData data;
		try {
			data = this.copyObject();
		} catch (CloneNotSupportedException e) {
			throw new DataOperationError(e);
		}

		Instances inst = data.getData();
		Set<Attribute> attrSaveFromDelSet = new HashSet<>();

		for (int index : indexes) {
			Attribute attrSaveFromDelete = inst.attribute(index);
			attrSaveFromDelSet.add(attrSaveFromDelete);
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
	public boolean isMultiAttribute() {
		return true;
	}

	WekaData copyObject() throws CloneNotSupportedException {
		WekaData data = (WekaData) dataFactory.getData(WekaData.class);
		data.setData(new Instances(this.getData()));
		return data;

	}

	@Override
	public double[] plot() {
		// TODO
		return this.getAllValuesAsDoubleAt("Account length");
	}

	@Override
	public String title() {
		// TODO
		return "Account length";
	}

	@Override
	public Data<Instances> getSubset(int from, int to) {
		WekaData newData = (WekaData) dataFactory.getData(WekaData.class);
		newData.data.addAll(this.data.subList(from, to));
		return newData;
	}

	@Override
	public int getInstancesCount() {
		return super.getData().numInstances();
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

}
