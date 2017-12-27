package evm.dmc.core.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FrameworkType;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionType;
import evm.dmc.core.DMCCoreConfig;
import evm.dmc.core.TestUtils;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.weka.WekaFramework;
import evm.dmc.weka.function.WekaCSVLoad;
import evm.dmc.weka.function.WekaFunctions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DMCCoreConfig.class, FrameworksService.class, DataMiningCombinerApplicationTests.class})
//@SpringBootTest	// https://github.com/spring-projects/spring-boot/issues/10465
@DataJpaTest
@Rollback
@ComponentScan( basePackages = { "evm.dmc.core", "evm.dmc.model", "evm.dmc.api.model"})
//@SpringBootConfiguration
//@AutoConfigurationPackage
public class FrameworksServiceImplTest {
	
	@Autowired
	FrameworksService repo;

	@Test
	public void testGetFrameworksDescriptors() {
		assertNotNull(repo);
		Set<String> names = repo.getFrameworksDescriptors();
		assertThat(names, not(empty()));
		System.out.println(Arrays.toString(names.toArray()));

		assertThat(names, hasItems("wekaFramework"));
	}
	
	@Test
	public final void testGetFramework() {
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
	public void testGetFunction() {
		DMCFunction func = repo.getFunction(WekaFunctions.CSVLOADER);
		
		assertNotNull(func);
		assertTrue(func instanceof WekaCSVLoad);
		 
	}
	
	@Test
	public void testGetFunctionByModel() {
		FunctionModel fnModel = new FunctionModel();
		fnModel.setFramework(repo.getFramework("wekaFramework").getFrameworkModel());
		fnModel.setName(WekaFunctions.KMEANS);
		fnModel.setType(FunctionType.CSV_DATASOURCE);
		DMCFunction<?> fun = repo.getFunction(fnModel);
		assertThat(fun.getName(), equalTo(WekaFunctions.KMEANS));
	}


}
