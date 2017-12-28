package evm.dmc.weka;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.api.model.FrameworkModel;
import evm.dmc.core.DataFactory;
import evm.dmc.core.TestUtils;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Framework;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.function.WekaCSVLoad;
import evm.dmc.weka.function.WekaCSVSave;
import evm.dmc.weka.function.WekaFunctions;

@RunWith(SpringRunner.class)
//@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
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

		assertThat(names, hasItems(WekaFunctions.KMEANS, WekaFunctions.NORMALIZATION, WekaFunctions.STANDARDIZATION));
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
				// assertThat(properties.values(),
				// hasItem(func.getDescription()));
			}
		}

	}
	
	@Test
	public final void testGetSaverDescriptors() {
		assertNotNull(wekaFW);
		Map<String, Class<?>> descMap = wekaFW.getSaverDescriptors();
//		Iterator<Entry<String, Class<?>>> iter = descMap.entrySet().iterator();
//		while(iter.hasNext()){
//			Entry<String, Class<?>> entr = iter.next();
//			System.out.println(entr.getKey() + " - " + entr.getValue());
//		}
		System.out.println(TestUtils.printMap(descMap));
		assertThat(descMap, hasEntry("Weka_CSVSaver", WekaCSVSave.class));
	}
	
	@Test
	public final void testGetLoaderDescriptors() {
		assertNotNull(wekaFW);
		Map<String, Class<?>> descMap = wekaFW.getLoaderDescriptors();
		Iterator<Entry<String, Class<?>>> iter = descMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Class<?>> entr = iter.next();
			System.out.println(entr.getKey() + " - " + entr.getValue());
		}
		assertThat(descMap, hasEntry("Weka_CSVLoader", WekaCSVLoad.class));
	}
	
	@Test
	public final void testGetFrameworkModel() {
		assertNotNull(wekaFW);
		FrameworkModel model = wekaFW.getFrameworkModel();
		assertNotNull(model);
		assertFalse(model.getFunctions().isEmpty());
		System.out.println(model.getName());
		assertEquals("wekaFramework", model.getName());
		System.out.println(model.getFunctions().iterator().next().getName());
	}

}
