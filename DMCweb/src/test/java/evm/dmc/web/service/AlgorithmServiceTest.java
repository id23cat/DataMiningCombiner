package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.algorithm.FWMethod;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.core.api.AttributeType;
import evm.dmc.web.exceptions.FunctionNotFoundException;
import evm.dmc.web.service.dto.TreeNodeDTO;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Rollback
@Slf4j
public class AlgorithmServiceTest extends ServiceTest{
	private static final String WEKA_PCA = "Weka_PCA";
	private static final String WEKA_KMEANS = "Weka_KMeansClustering";
	
	@Autowired
	AlgorithmService algorithmService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	MetaDataService metaDataService;
	
	@Autowired
	private FrameworkFrontendService frameworkService;
	
	@Autowired
	EntityManager em;

		
	@Test
	public final void testGetForProjectEmty() {
		ProjectModel project = projectService.getByName(ServiceTest.PROJECTNAME_1).findFirst().get();
		Set<Algorithm> algStream = algorithmService.getForProject(project);
		assertTrue(algStream.isEmpty());
	}
	
	@Test
	public final void testSetAttributes_NotModified() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);
		Optional<MetaData> optMetaData = metaDataService.getByProjectAndName(project_1, DATASET_IRIS);
		assertTrue(optMetaData.isPresent());
		
		algorithmService.setAttributes(algorithm, optMetaData.get());
		
		assertThat(algorithm.getSrcAttributes(), equalTo(optMetaData.get().getAttributes()));
		
	}
	
	@Test
	public final void testSetAttributes_Modified() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);
		String keyName = "1";
		Optional<MetaData> optMetaData = metaDataService.getByProjectAndName(project_1, DATASET_IRIS);
		assertTrue(optMetaData.isPresent());
		
		DataAttribute newAttribute = DataAttribute.builder()
				.name(keyName)
				.multiplier(25.)
				.type(AttributeType.STRING)
				.build();
		MetaData metaData = optMetaData.get().toBuilder()
				.attributes(new HashMap<String, DataAttribute>(optMetaData.get().getAttributes()))
				.build();
		metaData.getAttributes().put(keyName, newAttribute);
		
		algorithmService.setAttributes(algorithm, metaData);
		
		assertThat(algorithm.getSrcAttributes(), not(equalTo(optMetaData.get().getAttributes())));
		
	}
	
//	@Test
//	public final void testGetFrameworksList() {
//		List<FrameworkModel> frameworks = algorithmService.getFrameworksList();
//		
//		log.debug("Frameworks: {}", frameworks);
//		for(FrameworkModel fm: frameworks) {
//			log.debug("Functions: {}", fm.getFunctions());
//		}
//		
//	}
	
	@Test
	public final void testAddMethod() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);

		Optional<FunctionModel> optFunction0 = frameworkService.getFunction(WEKA_PCA); 
		assertTrue(optFunction0.isPresent());
		Optional<TreeNodeDTO> optDTO0 = FrameworkFrontendService.functionToDTO(optFunction0);
		assertTrue(optDTO0.isPresent());
		
		Optional<FunctionModel> optFunction1 = frameworkService.getFunction(WEKA_KMEANS); 
		assertTrue(optFunction1.isPresent());
		Optional<TreeNodeDTO> optDTO1 = FrameworkFrontendService.functionToDTO(optFunction1);
		assertTrue(optDTO1.isPresent());
		
		int originalSize = algorithm.getMethod()==null? 0 : algorithm.getMethod().getSteps().size();
		algorithm = algorithmService.addMethod(algorithm, optDTO0.get());
		PatternMethod method0 = algorithm.getMethod().getSteps().get(algorithm.getMethod().getSteps().size()-1);
//		em.merge(algorithm);
		assertNotNull(method0);
		
		PatternMethod mainMethod = algorithm.getMethod();
		
		 algorithm = algorithmService.addMethod(algorithm, optDTO1.get());
		 PatternMethod method1 = algorithm.getMethod().getSteps().get(algorithm.getMethod().getSteps().size()-1);
//		em.merge(algorithm);
		assertNotNull(method1);
		
		assertThat(algorithm.getMethod().getSteps().size(), equalTo(originalSize + 2));
		assertThat(algorithm.getMethod(), equalTo(mainMethod));
	}
	
	@Test(expected=FunctionNotFoundException.class)
	public final void testAddMethod_nonExistingFunction() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);
		TreeNodeDTO nExFunction = TreeNodeDTO.builder()
				.id(-1234L)
				.text("Uncknown")
				.build();
		
		algorithmService.addMethod(algorithm, nExFunction);
	}
	
	@Test
	public final void testSetMethod() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);

		Optional<FunctionModel> optFunction0 = frameworkService.getFunction(WEKA_PCA); 
		assertTrue(optFunction0.isPresent());
		Optional<TreeNodeDTO> optDTO0 = FrameworkFrontendService.functionToDTO(optFunction0);
		assertTrue(optDTO0.isPresent());
		
		Optional<FunctionModel> optFunction1 = frameworkService.getFunction(WEKA_KMEANS); 
		assertTrue(optFunction1.isPresent());
		Optional<TreeNodeDTO> optDTO1 = FrameworkFrontendService.functionToDTO(optFunction1);
		assertTrue(optDTO1.isPresent());
		
		int index = 0;
		
		algorithm = algorithmService.setMethod(algorithm, optDTO0.get(), index);
		PatternMethod method0 = algorithm.getMethod().getSteps().get(index);
		assertNotNull(method0);
		
		PatternMethod mainMethod = algorithm.getMethod();
		
		algorithm = algorithmService.setMethod(algorithm, optDTO1.get(), 0); 
		PatternMethod method1 = algorithm.getMethod().getSteps().get(index);
		assertNotNull(method1);
		
		assertThat(algorithm.getMethod(), equalTo(mainMethod));
		assertThat(algorithm.getMethod().getSteps(), hasItem(method1));
		assertThat(algorithm.getMethod().getSteps(), not(hasItem(method0)));
		
		
	}
	
	private Algorithm getAlgorithm(ProjectModel project, String algNamem, String setDataSource) {
		Optional<Algorithm> optAlg = algorithmService.getByProjectAndName(project, algNamem);
		assertTrue(optAlg.isPresent());
//		return algorithmService.setDataSource(optAlg.get(), setDataSource);
		return optAlg.get();
	}

}
