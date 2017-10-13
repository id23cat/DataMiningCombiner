package evm.dmc.web.project;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.web.hello.HelloController;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProjectPropertiesController.class, secure = false)
public class ProjectPropertiesControllerTest {

	@Test
	@Ignore
	public final void testNewProject() {
		fail("Not yet implemented"); // TODO
	}

}
