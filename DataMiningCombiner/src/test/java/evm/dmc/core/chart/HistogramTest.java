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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.weka.WekaTestBaseClass;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@DataJpaTest  // TODO: remove where unneeded
public class HistogramTest extends WekaTestBaseClass {

	@Autowired
	Histogram histogram;

	@Test
	public final void testSaveToPng() throws IOException {
		histogram.setAttribIndexesToPlot(data.getIndexByName("Account length"));
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
		histogram
				.setAttribIndexesToPlot(data.getIndexByName("State"), data.getIndexByName("Account length"),
						data.getIndexByName("Total day minutes"), data.getIndexByName("Total day calls"))
				.setWidth(2048);

		List<String> list = histogram.saveToPng(data, "Data/testSeveral");
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
		histogram.setAttribIndexesToPlot(data.getIndexByName("Account length"));
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

}
