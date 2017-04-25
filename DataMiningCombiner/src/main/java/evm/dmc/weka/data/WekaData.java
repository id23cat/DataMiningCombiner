package evm.dmc.weka.data;

import java.io.File;

import evm.dmc.core.data.Data;
import evm.dmc.core.data.FromFileLoadable;
import evm.dmc.core.data.InMemoryData;
import evm.dmc.core.data.ToFileStorable;
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
public class WekaData extends InMemoryData<Instances> implements FromFileLoadable, ToFileStorable {

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

}
