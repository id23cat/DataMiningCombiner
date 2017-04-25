package evm.dmc.core;

public interface InContextExecutable {
	/**
	 * Returns object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	FrameworkContext getContext();

	/**
	 * Sets object that should be used in function object as context of
	 * execution
	 * 
	 * @return
	 */
	void setContext(FrameworkContext context);

}
