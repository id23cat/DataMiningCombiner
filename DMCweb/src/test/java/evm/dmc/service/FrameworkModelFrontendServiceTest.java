package evm.dmc.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FrameworkType;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.web.exceptions.FrameworkNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@Transactional
@ActiveProfiles({"test", "devH2"})
@Rollback
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class FrameworkModelFrontendServiceTest {
	@Autowired
	private FrameworkModelFrontendService service;

	@Test
	public final void testGetFramework() throws FrameworkNotFoundException {
		String wekaName = "wekaFramework";
		assertNotNull(service);
		FrameworkModel wekaModel = new FrameworkModel();
		wekaModel.setName(wekaName);
		wekaModel.setType(FrameworkType.LOCAL);
		wekaModel.setActive(true);
//		FrameworkModel wekaFramework = service.getFramework("wekaFramework")
//				.orElseThrow(()-> new FrameworkNotFoundException());
//		assertThat(wekaFramework.getName(), equalTo("wekaFramework"));
//		log.debug("=== Framework {}", wekaFramework.getName()+" "+wekaFramework.getType());
		Optional<FrameworkModel> wekaFramework = service.getFramework(wekaName);
		assertTrue(wekaFramework.isPresent());
		log.debug("=== {}", wekaFramework.get().toString());
		assertTrue(wekaFramework.get().isSame(wekaModel));
	}

	@Test
	public final void testGetFunction() {
		Optional<FunctionModel> function = service.getFunction("Weka_KMeansClustering");
		assertTrue(function.isPresent());
		assertThat(function.get().getName(), equalTo("Weka_KMeansClustering"));
	}

	@Test
	public final void testFindFunctionByWord() {
		Stream<FunctionModel> functions = service.findFunctionByWord("cluster");
		assertTrue(functions.anyMatch((fun) -> Objects.equals(fun.getName(), "Weka_KMeansClustering")));
	}

	@Test
	public final void testGetAllFunctions() {
		Stream<FunctionModel> functions = service.getAllFunctions();
		
		functions.forEach((FunctionModel fun) -> {
			assertFalse(fun.getName().isEmpty());
			log.debug("== Function: {}", fun.getName());
		});
		
//		assertFalse(functions.count() <= 0);
		
	}

	@Test
	public final void testGetDataLoaders() {
		Stream<FunctionSrcModel> loaders = service.getDataLoaders();
		assertFalse(loaders.count() <= 0);
		
	}

	@Test
	public final void testGetDataSavers() {
		Stream<FunctionDstModel> savers = service.getDataSavers();
		assertFalse(savers.count() <= 0);
	}

}
