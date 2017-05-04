package evm.dmc.python;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import evm.dmc.core.AbstractFramework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.DMCFunction;
import evm.dmc.python.function.AbstractPythonFunction;

@Service
@PythonFW
public class PythonFramework extends AbstractFramework {

	/**
	 * Normally sould be setted by Spring context from jep.properties
	 */
	@Value("${jep.javalibpath}")
	private String jepLibPath;

	private static Class CLASS = AbstractPythonFunction.class;

	/**
	 * @param jEP_LBRARY_PATH
	 *            the jEP_LBRARY_PATH to set
	 */
	public void setJepLibPath(String jepLibPath) {
		this.jepLibPath = jepLibPath;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.Framework#initFramework() Sets default abstract
	 * function type as AbstractPythonFunction.class
	 */
	@Override
	public void initFramework() {
		super.initFrameworkForType(CLASS);

	}

	public PythonFramework() {
		// super(AbstractPythonFunction.class);
	}

	@Override
	public Data getData(Class type) {
		return super.instantiateData(type);
	}

	@Override
	public DMCFunction getDMCFunction(String descriptor) {
		DMCFunction function = super.getDMCFunction(descriptor);
		return function;
	}

	@Override
	protected Class getFunctionClass() {
		return CLASS;
	}

}
