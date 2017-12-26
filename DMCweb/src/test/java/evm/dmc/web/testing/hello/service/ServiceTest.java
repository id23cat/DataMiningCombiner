package evm.dmc.web.testing.hello.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.config.ApplicationConfiguration;
import evm.dmc.service.testing.Service;

@RunWith(SpringRunner.class)
@SpringBootTest("service.message=Hello")
@Ignore
public class ServiceTest {
	@Autowired
	private Service service;

	@Test
	public void contextLoads() {
		assertThat(service.message()).isNotNull();
	}

	@SpringBootApplication
	@Import(ApplicationConfiguration.class)
	static class TestConfiguration {
	}

}
