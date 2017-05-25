package evm.dmc.core.chart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import evm.dmc.weka.WekaTestBaseClass;

@RunWith(SpringRunner.class)
public class HistogramTest extends WekaTestBaseClass {

	@Autowired
	Histogram histogram;

	@Test
	public final void testSaveToPng() throws IOException {
		data.setAttributesToPlot(data.getIndexByName("Account length"));
		List<String> list = histogram.saveToPng(data, "Data/test");
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertNotNull(list.get(0));
		File file = new File(list.get(0));
		assertTrue(file.length() > 0);
		file.delete();

	}

	@Test
	public final void testSaveToPngSeveralArgs() throws IOException {
		data.setAttributesToPlot(data.getIndexByName("State"), data.getIndexByName("Account length"),
				data.getIndexByName("Total day minutes"), data.getIndexByName("Total day calls"));
		List<String> list = histogram.saveToPng(data, "Data/test");
		assertFalse(list.isEmpty());
		assertEquals(4, list.size());
		for (String fname : list) {
			assertNotNull(fname);
			File file = new File(fname);
			assertTrue(file.length() > 0);
			// file.delete();
		}

	}

	@Test
	public final void testGetBufferedImage() {
		data.setAttributesToPlot(data.getIndexByName("Account length"));
		List<BufferedImage> list = histogram.getBufferedImage(data);

		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertNotNull(list.get(0));
		assertNotNull(list.get(0).getData().getDataBuffer());

		// this gets the actual Raster data as a byte array
		// int[] byteArrayToCheck = ((DataBufferInt)
		// histogram.getBufferedImage(data).get(0).getData().getDataBuffer())
		// .getData();

		// assertArrayEquals(byteArrayTest, byteArrayToCheck);
	}

	// @Test
	// public final void testHistogramChart() throws Exception {
	// Histogram hist = Histogram.getPlotter();
	//
	// String fname = hist.saveToPng(data, "Data/test");
	// File file = new File(fname);
	// assertTrue(file.exists());
	//
	// file.delete();
	// }
	//
	// public final List<BufferedImage> tstHistBuffered(int... indexes) throws
	// Exception {
	// List<BufferedImage> list = new ArrayList<>();
	// for (int index : indexes) {
	// String plotTitle = "Account length";
	// double[] values = data.getAllValuesAsDoubleAt("Account length");
	// HistogramDataset dataset = new HistogramDataset();
	// dataset.setType(HistogramType.FREQUENCY);
	// dataset.addSeries("Histogram", values, 3333);
	//
	// String xaxis = "number";
	// String yaxis = "value";
	// PlotOrientation orientation = PlotOrientation.VERTICAL;
	// boolean show = false;
	// boolean toolTips = false;
	// boolean urls = false;
	// JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
	// dataset, orientation, show,
	// toolTips, urls);
	// int width = 1024;
	// int height = 768;
	// list.add(chart.createBufferedImage(width, height));
	// }
	// return list;
	// }
	//
	// public final List<String> tstHistFile(int... indexes) throws Exception {
	// String plotTitle = "Account length";
	// double[] values = data.getAllValuesAsDoubleAt("Account length");
	// HistogramDataset dataset = new HistogramDataset();
	// dataset.setType(HistogramType.FREQUENCY);
	// dataset.addSeries("Histogram", values, 3333);
	// String plotTitle = "Account length";
	// String xaxis = "number";
	// String yaxis = "value";
	// PlotOrientation orientation = PlotOrientation.VERTICAL;
	// boolean show = false;
	// boolean toolTips = false;
	// boolean urls = false;
	// JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
	// dataset, orientation, show, toolTips,
	// urls);
	// int width = 1024;
	// int height = 768;
	// ChartUtilities.saveChartAsPNG(new File("Data/histogram.PNG"), chart,
	// width, height);
	// return chart.createBufferedImage(width, height);
	// }

}
