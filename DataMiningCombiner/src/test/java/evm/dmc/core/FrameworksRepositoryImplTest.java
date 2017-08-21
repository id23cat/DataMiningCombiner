package evm.dmc.core;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.function.WekaCSVLoad;
import evm.dmc.weka.function.WekaFunctions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCCoreConfig.class)
public class FrameworksRepositoryImplTest {
	@Autowired
	FrameworksRepository repo;

	@Test
	public void testGetFrameworksDescriptors() {
		assertNotNull(repo);
		Set<String> names = repo.getFrameworksDescriptors();
		assertThat(names, not(empty()));
		System.out.println(Arrays.toString(names.toArray()));

		assertThat(names, hasItems("wekaFramework"));
	}

	@Test
	public void testGetFramework() {
		Framework fw = repo.getFramework("wekaFramework");
		assertNotNull(fw);
		assertTrue(fw instanceof WekaFramework);
		System.out.println(fw.getFunctionDescriptors());
		System.out.println(fw.getLoaderDescriptors());
		System.out.println(fw.getSaverDescriptors());
	}

	@Test
	public void testGetFunctionsDescriptors() {
		Map<String, String> fdescriptors = repo.getFunctionsDescriptors();
		assertThat(fdescriptors.values(), not(empty()));

		System.out.println(TestUtils.printMap(fdescriptors));
	}

	@Test
	public void testGetFunctionsDescriptions() {
		Map<String, String> descriptions = repo
				.getFunctionsDescriptions(repo.getFunctionsDescriptors().keySet());
		assertThat(descriptions.keySet(), not(empty()));
		assertThat(descriptions.values(), not(empty()));
		for (String desc : descriptions.values())
			assertThat(desc.isEmpty(), is(false));
		System.out.println(TestUtils.printMap(descriptions));
	}
	
	@Test
	public void testFilterFunction() {
		Map<String, String> map = new HashMap<>();
		map.put("Some word","Value 1");
		map.put("Another_word", "value 2");
		map.put("Bad string", "Value 3");
		map.put("The last word", "value 4");
		
		repo.filterFunction(map, "word");
		System.out.println(TestUtils.printMap(map));
		
		assertThat(map, not(hasEntry("Bad string","Value 3")));
		
	}
	
	@Test
	public void testFindFunctionByWord() {
		Map<String,String> map = repo.findFunctionByWord("csv");
		System.out.println(TestUtils.printMap(map));
		
		assertThat(map.keySet(), hasItems("Weka_CSVLoader", "Weka_CSVSaver"));
		repo.filterFunction(map, "load");
		assertThat(map.keySet(), both(hasItems("Weka_CSVLoader")).and(not(hasItems("Weka_CSVSaver"))));
	}
	
	@Test
	public void testGetFunction() {
		DMCFunction func = repo.getFunction(WekaFunctions.CSVLOADER);
		
		assertNotNull(func);
		assertTrue(func instanceof WekaCSVLoad);
		 
		
	}

}
