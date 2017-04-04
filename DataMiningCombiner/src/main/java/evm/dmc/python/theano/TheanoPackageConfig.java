package evm.dmc.python.theano;

import org.python.util.PythonInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import evm.dmc.core.DMCCoreConfig;

@Configuration
@ComponentScan(basePackageClasses = { DMCCoreConfig.class })
public class TheanoPackageConfig {
	@Bean
	PythonInterpreter getPythonInterpreter() {
		return new PythonInterpreter();
	}

}
