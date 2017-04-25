package evm.dmc.weka.function;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.data.WekaData;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Standardize;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaFunctionTest {
	WekaData data = new WekaData();
	@Value("${wekatest.datasource}")
	String souceFile;

	@Value("${wekatest.basedir}")
	String dir;

	@Value("${wekatest.filter}")
	String filterType;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testNormalize() throws Exception {
		weka.filters.Filter filter;
		data.load(souceFile);
		Instances inst = data.getData();

		filter = new Normalize();
		((Normalize) filter).setIgnoreClass(true);
		filter.setInputFormat(inst);
		inst = Filter.useFilter(inst, filter);
		data.setData(inst);
		data.store(dir + "/" + "_normalized.csv");

	}

	@Test
	public final void testStandardize() throws Exception {
		weka.filters.Filter filter;
		data.load(souceFile);
		Instances inst = data.getData();

		filter = new Standardize();
		((Standardize) filter).setIgnoreClass(true);
		filter.setInputFormat(inst);
		inst = Filter.useFilter(inst, filter);
		data.setData(inst);
		data.store(dir + "/" + "_standartized.csv");
	}

}
