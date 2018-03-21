package evm.dmc.web.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;

public class AlgorithmServiceTest extends ServiceTest{
	
	@Autowired
	AlgorithmService algorithmService;
	
	@Autowired
	ProjectService projectService;

		
	@Test
	public final void testGetForProjectEmty() {
		ProjectModel project = projectService.getByName(ServiceTest.PROJECTNAME_1).findFirst().get();
		Set<Algorithm> algStream = algorithmService.getForProject(project);
//		assertThat(algStream.count(), equalTo(0L));
		assertTrue(algStream.isEmpty());
	}

}
