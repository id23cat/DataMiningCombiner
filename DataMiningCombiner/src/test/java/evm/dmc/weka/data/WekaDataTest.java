package evm.dmc.weka.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import evm.dmc.core.chart.Histogram;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import weka.core.Instances;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaDataTest {
	// @Value("#{wekaFramework.getData(Weka_Instances.getClass())}")
	WekaData data;

	@Autowired
	WekaFramework fw;

	@Autowired
	CSVLoader csv;
	@Value("${wekatest.datasource}")
	String sourceFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void loadData() {
		assertNotNull(csv);
		assertThat(sourceFile, startsWith("Data"));
		csv.setSource(sourceFile);
		data = fw.castToWekaData(csv.get());
		// data.load(souceFile);
	}

	@Test
	public final void testLoad() throws Exception {
		assertNotNull(data);
		assertNotNull(data.getData());
		Instances inst = data.getData();
		int length = inst.numInstances();
		assertEquals(3333, length);

	}

	@Test
	public final void testInstances() throws Exception {
		// System.out.println(data.getData());
		Instances inst = data.getData();
		printInstancesInfo(inst);
	}

	@Test
	public final void testCopyObject() throws Exception {
		WekaData data2 = data.copyObject();
		assertThat(data2, is(not(sameInstance(data))));
		assertThat(data2.getData(), is(not(sameInstance(data.getData()))));

	}

	@Test
	public final void testGetAttribute() throws Exception {
		int attrNum = 0;
		Data<Instances> attr = data.getAttribute(attrNum);
		assertNotNull(attr);
		Instances inst = attr.getData();
		assertEquals(1, inst.numAttributes());
		assertEquals(data.getData().size(), inst.size());
		int index = new Random().nextInt(inst.size());
		assertEquals(data.getData().get(index).attribute(attrNum), inst.get(index).attribute(0));

	}

	@Test
	public final void testGetAttributeByName() throws Exception {
		WekaData attr = data.getAttribute("Voice mail plan");
		assertNotNull(attr);
		assertNotNull(attr.getData());
		assertEquals("Yes", attr.getValueAsString(1, 0));
	}

	@Test
	public final void testGetAttributes() throws Exception {
		int[] attrNums = { 1, 3, 5 };
		Data<Instances> attr = data.getAttributes(attrNums);
		assertNotNull(attr);
		Instances inst = attr.getData();
		assertEquals(attrNums.length, inst.numAttributes());
		assertEquals(data.getData().size(), inst.size());
		int otherIdx = 0;
		Random rnd = new Random();
		for (int idx : attrNums) {
			int randIndex = rnd.nextInt(inst.size());
			assertEquals(data.getData().get(randIndex).attribute(idx), inst.get(randIndex).attribute(otherIdx++));
		}

	}

	@Test
	public final void testAny() throws Exception {
		Instances inst = data.getData();
		WekaData data2 = data.copyObject();
		Instances instOrig = data2.getData();

		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		System.out.println(inst);
		printInstancesInfo(inst);

	}

	@Test
	public final void testHistogramChart() throws Exception {
		Histogram hist = Histogram.getPlotter();
		// this gets the actual Raster data as a byte array
		int[] byteArrayTest = ((DataBufferInt) this.testHistogram().getData().getDataBuffer()).getData();

		// this gets the actual Raster data as a byte array
		int[] byteArrayToCheck = ((DataBufferInt) hist.getBufferedImage(data).getData().getDataBuffer()).getData();
		assertArrayEquals(byteArrayTest, byteArrayToCheck);

		String fname = hist.saveToPng(data, "Data/test");
		File file = new File(fname);
		assertTrue(file.exists());

		file.delete();
	}

	@Test
	public final void testGetSubset() {
		WekaData subDat = data.getSubset(0, 1);
		assertEquals(2, subDat.getData().size());
		assertTrue(subDat.getData().get(0).attribute(1).isNumeric());
		assertThat(subDat.getData().get(0).value(1), closeTo(128, 0.01));

		assertTrue(subDat.getData().get(0).attribute(4).isNominal());
		int nomIndex = (int) subDat.getData().get(0).value(4);
		assertThat(subDat.getData().get(0).attribute(4).value(nomIndex), equalTo("Yes"));

		assertTrue(subDat.getData().get(1).attribute(1).isNumeric());
		assertThat(subDat.getData().get(1).value(1), closeTo(107, 0.01));

		assertTrue(subDat.getData().get(1).attribute(4).isNominal());
		int nomIndex2 = (int) subDat.getData().get(1).value(4);
		assertThat(subDat.getData().get(1).attribute(4).value(nomIndex2), equalTo("Yes"));

	}

	@Test
	public final void testGetInstance() {
		WekaData subDat = data.getInstance(2);
		assertEquals(1, subDat.getData().size());
		assertTrue(subDat.getData().get(0).attribute(1).isNumeric());
		assertThat(subDat.getData().get(0).value(1), closeTo(137, 0.01));

		assertTrue(subDat.getData().get(0).attribute(4).isNominal());
		int nomIndex = (int) subDat.getData().get(0).value(4);
		assertThat(subDat.getData().get(0).attribute(4).value(nomIndex), equalTo("No"));

	}

	@Test(expected = evm.dmc.weka.exceptions.IndexOutOfRange.class)
	public final void testGetInstanceWrongIndex() {
		WekaData subDat = data.getInstance(3333);
	}

	@Test
	public final void testClone() {
		WekaData cln = data.clone();
		assertNotEquals(data, cln);
		assertEquals(data.getValue(1, 2), cln.getValue(1, 2), 0.0001);
		assertEquals(data.getValueAsString(1, 3), cln.getValueAsString(1, 3));
	}

	@Test
	public final void testGetValue() {
		assertEquals(107, data.getValue(1, 1), 0.0001);
		assertEquals(0.0, data.getValue(1, 3), 0.0001);
	}

	@Test
	public final void testGetValueAsString() {
		assertEquals("No", data.getValueAsString(1, 3));
		assertEquals("OH", data.getValueAsString(3, 0));
		assertEquals("415.0", data.getValueAsString(2, 2));
	}

	@Test(expected = evm.dmc.weka.exceptions.IndexOutOfRange.class)
	public final void testUsingWrongIndex() {
		data.isDate(3333);
	}

	@Test
	public final void testTypes() {
		assertTrue(data.isNumeric(1));
		assertTrue(data.isNumeric(2));
		assertTrue(data.isNumeric(5));

		assertTrue(data.isNominal(0));
		assertTrue(data.isNominal(3));
	}

	public final BufferedImage testHistogram() throws Exception {
		double[] values = data.getAllValuesAsDoubleAt("Account length");
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Histogram", values, 3333);
		String plotTitle = "Account length";
		String xaxis = "number";
		String yaxis = "value";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips,
				urls);
		int width = 1024;
		int height = 768;
		ChartUtilities.saveChartAsPNG(new File("Data/histogram.PNG"), chart, width, height);
		return chart.createBufferedImage(width, height);
	}

	void printInstancesInfo(Instances inst) {
		for (int i = 0; i < inst.numAttributes(); i++) {
			System.out.println("[" + i + "]");
			System.out.println("\t" + inst.attribute(i));
			System.out.println("\t" + inst.attribute(i).enumerateValues());
			System.out.println("\t" + inst.attribute(i).weight());
			System.out.println("\t" + inst.attributeStats(i));

		}
	}
}
