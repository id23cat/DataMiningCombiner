package evm.dmc.core;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static evm.dmc.core.CaseInsensitiveSubstringMatcher.containsIgnoringCase;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.FunctionDstModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionSrcModel;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.weka.function.WekaFunctions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCCoreConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class SerialAlgorithmTest {
	Algorithm algorithm = null;
	Map<String, String> loadersMap = null;
	Map<String, String> saversMap = null;
	Map<String, String> functionsMap = null;

	@Autowired
	FrameworksRepository repo = null;

	@Value("${wekatest.datasourceSmall}")
	String file;

	String destFile = "temp.csv";

	@PostConstruct
	public void init() {
		loadersMap = repo.filterFunctionMap(repo.getDataLoadersDescriptorsMap(), "csv");
		saversMap = repo.filterFunctionMap(repo.getDataSaversDescriptorsMap(), "csv");
		functionsMap = repo.getFunctionsDescriptors();
	}

	@Before
	public void before() {
		assertNotNull(repo);
		algorithm = new SerialAlgorithm(repo);

		DMCDataLoader loader = (DMCDataLoader) repo
				.getFunction(loadersMap.keySet().stream().findFirst().orElse(WekaFunctions.CSVLOADER));
		loader.setSource(file);
		algorithm.addDataSource(loader);
		algorithm.addCommand(repo.getFunction(repo.filterFunctionMap(functionsMap, "standar").keySet().stream()
				.findFirst().orElse(WekaFunctions.STANDARDIZATION)));

		algorithm.addCommand(repo.getFunction(
				repo.filterFunctionMap(functionsMap, "pca").keySet().stream().findFirst().orElse(WekaFunctions.PCA)));
		DMCDataSaver saver = (DMCDataSaver) repo
				.getFunction(saversMap.keySet().stream().findFirst().orElse(WekaFunctions.CSVSAVER));
		saver.setDestination(destFile);
		algorithm.addDataDestination(saver);

	}

	@Test
	public final void testAddCommandString() {
		AlgorithmModel algModel = algorithm.getModel();

		// check that it contains loader function
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check that it contains correct source file name
		assertThat(algModel.getDataSource().getSource(),
				both(equalTo(algModel.getDataSource().getProperties().getProperty(FunctionSrcModel.SRC_PROPERTY_NAME)))
						.and(equalTo(file)));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertFalse(funcIterator.hasNext());

		// check that it contains saver function
		assertThat(algModel.getDataDestination().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("save")));

		// check that it contains correct destination file name
		assertThat(algModel.getDataDestination().getDestination(),
				both(equalTo(
						algModel.getDataDestination().getProperties().getProperty(FunctionDstModel.DST_PROPERTY_NAME)))
								.and(equalTo(destFile)));

		algorithm.addCommand(
				repo.filterFunctionMap(repo.getFunctionsDescriptors(), "cluster").keySet().stream().findFirst().get());

		// check count and content of functions List
		funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("cluster"));
		assertFalse(funcIterator.hasNext());

	}

	@Test
	public final void testDelCommandString() {
		// deletion of the PCA function
		algorithm.delCommand(algorithm.getModel().getFunctions().get(1));
		Iterator<FunctionModel> funcIterator = algorithm.getModel().getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertFalse(funcIterator.hasNext());

		algorithm.delCommand(algorithm.getModel().getFunctions().get(0));
		funcIterator = algorithm.getModel().getFunctions().iterator();
		assertFalse(funcIterator.hasNext());

		algorithm.addCommand(
				repo.filterFunctionMap(repo.getFunctionsDescriptors(), "cluster").keySet().stream().findFirst().get());
		assertTrue(funcIterator.hasNext());
	}

	@Test
	public final void testExecute() throws IOException {
		algorithm.execute();
		File resultFile = new File(destFile);
		assertTrue(resultFile.length() != 0);
		
		resultFile.delete();
	}

	@Test
	public final void testInsertCommandAfterStringDMCFunctionOfQ() {
		algorithm.insertCommandAfter(repo.filterFunctionMap(repo.getFunctionsDescriptors(), "cluster").keySet().stream().findFirst().get(), 
				algorithm.getFunctionsList().get(0));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algorithm.getModel().getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("cluster"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertFalse(funcIterator.hasNext());
	}
	
	@Test
	public final void testInsertCommandBefore() {
		algorithm.insertCommandBefore(repo.filterFunctionMap(repo.getFunctionsDescriptors(), "cluster").keySet().stream().findFirst().get(), 
				algorithm.getFunctionsList().get(0));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algorithm.getModel().getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("cluster"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertFalse(funcIterator.hasNext());
	}

	@Test
	public final void testSetModel() {
		Algorithm alg = new SerialAlgorithm(repo);
		alg.setModel(algorithm.getModel());
		
		AlgorithmModel algModel = alg.getModel();

		// check that it contains loader function
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check that it contains correct source file name
		assertThat(algModel.getDataSource().getSource(),
				both(equalTo(algModel.getDataSource().getProperties().getProperty(FunctionSrcModel.SRC_PROPERTY_NAME)))
						.and(equalTo(file)));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("standar"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertFalse(funcIterator.hasNext());

		// check that it contains saver function
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check that it contains correct destination file name
		assertThat(algModel.getDataDestination().getDestination(),
				both(equalTo(
						algModel.getDataDestination().getProperties().getProperty(FunctionDstModel.DST_PROPERTY_NAME)))
								.and(equalTo(destFile)));
	}

}
