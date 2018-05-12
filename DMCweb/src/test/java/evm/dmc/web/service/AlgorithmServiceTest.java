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
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.core.api.AttributeType;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class AlgorithmServiceTest extends ServiceTest{
	
	@Autowired
	AlgorithmService algorithmService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	MetaDataService metaDataService;
	
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
	
	@Test
	public final void testGetFrameworksList() {
		List<FrameworkModel> frameworks = algorithmService.getFrameworksList();
		
		log.debug("Frameworks: {}", frameworks);
		for(FrameworkModel fm: frameworks) {
			log.debug("Functions: {}", fm.getFunctions());
		}
		
	}
	
	private Algorithm getAlgorithm(ProjectModel project, String algNamem, String setDataSource) {
		Optional<Algorithm> optAlg = algorithmService.getByProjectAndName(project, algNamem);
		assertTrue(optAlg.isPresent());
		return algorithmService.setDataSource(optAlg.get(), setDataSource);
	}

}
