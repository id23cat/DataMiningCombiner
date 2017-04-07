package evm.dmc.python.theano;

import org.python.util.PythonInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import evm.dmc.core.DMCCoreConfig;

@Configuration
@Import(DMCCoreConfig.class)
public class TheanoPackageConfig {
	@Bean
	PythonInterpreter getPythonInterpreter() {
		return new PythonInterpreter();
	}

}
