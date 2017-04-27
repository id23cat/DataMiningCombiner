package evm.dmc.weka;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.DataFactory;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.weka.data.WekaData;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaFrameworkTest {
	@WekaFW
	@Autowired
	Framework wekaFW;

	@WekaFW
	@Autowired
	DataFactory wekaDF;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testGetData() {
		assertNotNull(wekaFW);
		assertTrue(wekaFW instanceof DataFactory);

		DataFactory dBuilder = (DataFactory) wekaFW;
		Data wData = dBuilder.getData(WekaData.class);
		assertNotNull(wData);
		assertNull(wData.getData());
		// assertThat(wData.getData(), instanceOf(Instances.class));

		assertNotNull(wekaDF);
		assertTrue(wekaDF instanceof DataFactory);
		Data wData2 = wekaDF.getData(WekaData.class);
		assertNotNull(wData2);
		assertNull(wData2.getData());
	}

}
