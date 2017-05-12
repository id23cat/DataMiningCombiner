package evm.dmc.weka.function;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	WekaCSVLoad csv;

	// @Value("#{wekaFramework.getData(Weka_Instances.getClass())}")
	WekaData data;
	@Value("${wekatest.datasource}")
	String souceFile;

	@Value("${wekatest.basedir}")
	String dir;

	@Value("${wekatest.filter}")
	String filterType;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void init() {
		csv.setSource(souceFile);
		data = (WekaData) csv.get();
		assertNotNull(data);
	}

	@Test
	public final void testNormalize() throws Exception {
		weka.filters.Filter filter;

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

		Instances inst = data.getData();
		filter = new Standardize();
		((Standardize) filter).setIgnoreClass(true);
		filter.setInputFormat(inst);
		inst = Filter.useFilter(inst, filter);
		data.setData(inst);
		data.store(dir + "/" + "_standartized.csv");
	}

	// @Test
	// public final void testPlotting() throws Exception {
	// data.load(souceFile);
	// final JFrame jf = new JFrame("AttribVisualization");
	// AttributeVisualizationPanel ap = new AttributeVisualizationPanel();
	// ap.setInstances(data.getData());
	// ap.setAttribute(1);
	// jf.setSize(500, 300);
	// jf.getContentPane().setLayout(new BorderLayout());
	// jf.getContentPane().add(ap, BorderLayout.CENTER);
	// jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// jf.setVisible(true);
	// Thread.sleep(5000);
	//
	// }

}
