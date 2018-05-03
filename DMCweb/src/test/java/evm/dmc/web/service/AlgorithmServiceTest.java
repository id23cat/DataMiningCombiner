package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.core.api.AttributeType;

@RunWith(SpringRunner.class)
public class AlgorithmServiceTest extends ServiceTest{
	
	@Autowired
	AlgorithmService algorithmService;
	
	@Autowired
	ProjectService projectService;
	
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
		MetaData metaData = algorithm.getDataSource();
		
		
		algorithmService.setAttributes(algorithm, metaData);
		
		assertThat(algorithm.getSrcAttributes(), equalTo(algorithm.getDataSource().getAttributes()));
		
	}
	
	@Test
	public final void testSetAttributes_Modified() {
		Algorithm algorithm =  getAlgorithm(project_1, ALGORITHM_0, DATASET_IRIS);
		String keyName = "1";
		
		DataAttribute newAttribute = DataAttribute.builder()
				.name(keyName)
				.multiplier(25.)
				.type(AttributeType.STRING)
				.build();
		MetaData metaData = MetaData.builder()
				.name("test")
				.build();
		metaData.getAttributes().put(keyName,newAttribute);
		
		algorithmService.setAttributes(algorithm, metaData);
		
		assertThat(algorithm.getSrcAttributes(), not(equalTo(algorithm.getDataSource().getAttributes())));
		
	}
	
	private Algorithm getAlgorithm(ProjectModel project, String algNamem, String setDataSource) {
		Optional<Algorithm> optAlg = algorithmService.getByProjectAndName(project, algNamem);
		assertTrue(optAlg.isPresent());
		return algorithmService.setDataSource(optAlg.get(), setDataSource);
	}

}
