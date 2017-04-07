package evm.dmc.python;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import evm.dmc.core.DMCCoreConfig;
import jep.Jep;
import jep.JepException;

@Configuration
@ComponentScan
@Import(DMCCoreConfig.class)
@PropertySource("classpath:jep.properties")
public class DMCPythonConfig {
	@Autowired
	Environment env;

	@Value("${jep.scriptsFolder}")
	private String scriptsFolder;

	@Bean
	public Jep jep() throws JepException {
		StringBuilder includePath = new StringBuilder(System.getProperty("user.dir"));
		includePath.append("/");
		includePath.append(scriptsFolder);

		// Tomcat uses a custom ClassLoader for each webapp. In order for Jep to
		// access any of the webapp's objects, they must share the ClassLoader.
		// http://jepp.sourceforge.net/usage.html
		return new Jep(false, includePath.toString(), Thread.currentThread().getContextClassLoader());
	}

}
