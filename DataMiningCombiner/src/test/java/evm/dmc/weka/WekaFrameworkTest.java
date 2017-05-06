package evm.dmc.weka;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import evm.dmc.core.DataFactory;
import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;
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

	@Test
	public final void testGettingFunctionsDescriptions() {
		assertNotNull(wekaFW);
		Set<String> names = wekaFW.getFunctionDescriptors();
		assertThat(names, not(empty()));
		System.out.println(Arrays.toString(names.toArray()));

		assertThat(names, containsInAnyOrder("Weka_KMeansClustering", "Weka_Normalize", "Weka_Standartize"));
	}

	@Test
	public final void testGetFunction() throws FileNotFoundException, IOException {
		Set<String> names = wekaFW.getFunctionDescriptors();
		assertThat(names, not(empty()));

		Properties properties = new Properties();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("weka.properties").getFile());

		try (InputStream input = new FileInputStream(file)) {
			properties.load(input);
			assertFalse(properties.isEmpty());

			System.out.println(Arrays.toString(properties.values().toArray()));

			for (String nm : names) {
				DMCFunction func = wekaFW.getDMCFunction(nm);
				assertEquals(nm, func.getName());
				assertThat(properties.values(), hasItem(func.getDescription()));
			}
		}

	}

}
