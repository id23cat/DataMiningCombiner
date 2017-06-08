package evm.dmc.python;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

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

	/**
	 * Normally should be setted by Spring context from jep.properties
	 */
	@Value("${jep.javalibpath}")
	private String jepLibPath;

	@Value("${jep.scriptsFolder}")
	private String scriptsFolder;

	/**
	 * Called after creation PythonFramework by Spring context
	 * Used to ensure that java.library.path contains path to jep system native
	 * libraries directory. Otherwise, gets path from JEP_LBRARY_PATH and adds
	 * it to java.library.path
	 * 
	 * @throws IOException
	 * @see <a href=
	 *      "http://nicklothian.com/blog/2008/11/19/modify-javalibrarypath-at-runtime/">Modify
	 *      java.library.path at runtime</a>
	 */
	@PostConstruct
	public void addDirToJavaLibraryPath() throws IOException {
		try {
			// This enables the java.library.path to be modified at runtime
			// From a Sun engineer at
			// http://forums.sun.com/thread.jspa?threadID=707176
			//
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (jepLibPath.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = jepLibPath;
			field.set(null, tmp);
			System.setProperty("java.library.path",
					System.getProperty("java.library.path") + File.pathSeparator + jepLibPath);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException("Failed to get field handle to set library path");
		}
	}

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
