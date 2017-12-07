package evm.dmc.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static evm.dmc.core.CaseInsensitiveSubstringMatcher.containsIgnoringCase;

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
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.ProjectType;
import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.Project;
import evm.dmc.core.api.SimplestProject;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCCoreConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class SimplestProjectImplTest {
	@Autowired
	@SimplestProject
	Project project = null;

	@Value("${wekatest.datasourceSmall}")
	String srcFile;

	String dstFile = "temp.csv";

	@Before
	public void before() {
		assertNotNull(project);
		project.addAlgorithm();
		
		ProjectModel model = project.getModel();
//		assertThat((Collection)model.getAlgorithms(), is(not(empty())));
		assertThat(model.getAlgorithms(), not(empty()));
		assertThat(model.getAlgorithms().iterator().next().getName(), containsIgnoringCase("serialalgorithm"));

		Algorithm alg = project.getAlgorithm();

		FunctionSrcModel srcModel = project.getDataSrcModel(getDescriptor(project.getDataLoadersSet(), "csv"));
		srcModel.setSource(srcFile);

		FunctionDstModel dstModel = project.getDataDstModel(getDescriptor(project.getDataSaversSet(), "csv"));
		dstModel.setDestination(dstFile);

		project.setDataSrc(alg, srcModel);
		project.addFunction(alg, project.getFunctionModel(getDescriptor(project.getFunctionsSet(), "normal")));
		project.addFunction(alg, project.getFunctionModel(getDescriptor(project.getFunctionsSet(), "pca")));
		project.setDataDst(alg, dstModel);
	}
	
	
	@Test
	public final void testSetModel() {
		assertThat(project.getModel().getAlgorithms().size(), equalTo(1));
		assertEquals(project.getAlgorithm().getModel(), project.getModel().getAlgorithms().iterator().next());
		
		ProjectModel model = project.getModel();

		AlgorithmModel algModel = model.getAlgorithms().iterator().next();
		
		System.out.println(String.format("model algoritms set size: %d", model.getAlgorithms().size()));
		
		ProjectModel newProjModel = new ProjectModel(model.getType(), model.getAlgorithms(), model.getProperties(), model.getProjectName());
		System.out.println(String.format("newProjectModel algoritms set size: %d", newProjModel.getAlgorithms().size()));
//		System.out.println(String.format("newAlgModel functions list size: %d", newAlgModel.getFunctions().size()));
		
		AlgorithmModel newAlgModel = newProjModel.getAlgorithms().iterator().next();
		
		System.out.println(String.format("newAlgModel functions list size: %d", newAlgModel.getFunctions().size()));
		// delete PCA function to determine difference with original model
		newAlgModel.delFunction(newAlgModel.getFunctions().get(1));
		
		project.setModel(newProjModel);
		algModel = project.getModel().getAlgorithms().iterator().next();
		
		assertEquals(project.getModel().getType(), ProjectType.SIMPLEST_PROJECT);
		
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("normal"));
		assertFalse(funcIterator.hasNext());

		// check that it contains saver function
		assertThat(algModel.getDataDestination().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("save")));
	}

	@Test
	public final void testCreateAlgorithm() throws IOException {
		Algorithm alg = project.getAlgorithm();

		AlgorithmModel algModel = alg.getModel();

		// check that it contains loader function
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("normal"));
		assertThat(funcIterator.next().getName(), containsIgnoringCase("pca"));
		assertFalse(funcIterator.hasNext());

		// check that it contains saver function
		assertThat(algModel.getDataDestination().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("save")));

		alg.execute();

		File resultFile = new File(dstFile);
		assertTrue(resultFile.length() != 0);

		resultFile.delete();

	}

	@Test
	public final void testCreateAlgorithmAlgorithmModel() {
		AlgorithmModel algModel = project.getAlgorithm().getModel();

		// delete PCA function to determine difference with original model
		algModel.delFunction(algModel.getFunctions().get(1));

		project.addAlgorithm(algModel);

		algModel = project.getAlgorithm().getModel();

		// check that it contains loader function
		assertThat(algModel.getDataSource().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("load")));

		// check count and content of functions List
		Iterator<FunctionModel> funcIterator = algModel.getFunctions().iterator();
		assertThat(funcIterator.next().getName(), containsIgnoringCase("normal"));
		// PCA function has been deleted
		assertFalse(funcIterator.hasNext());

		// check that it contains saver function
		assertThat(algModel.getDataDestination().getName(),
				both(containsIgnoringCase("csv")).and(containsIgnoringCase("save")));

	}

	private String getDescriptor(Set<String> descriptorsSet, String substring) {
		return descriptorsSet.stream().filter(funNmae -> funNmae.toLowerCase().contains(substring)).findFirst().get();
	}

}
