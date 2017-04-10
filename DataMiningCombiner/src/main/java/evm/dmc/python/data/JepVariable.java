package evm.dmc.python.data;

import java.util.Random;

import evm.dmc.core.data.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class JepVariable.
 */
public abstract class JepVariable implements Data<String> {

	/** The chars. */
	private static char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	/** The sb. */
	private static StringBuilder sb = new StringBuilder();

	private static int strlength = 15;

	/** The variableName . */
	protected String variableName;

	/**
	 * Instantiates a new jep variable. Sets deault variable name as random
	 * {@value #variableName} string characters string
	 */
	public JepVariable() {
		super();
		this.variableName = randomString();
	}

	/**
	 * Instantiates a new jep variable.
	 *
	 * @param variable
	 *            the variable
	 */
	public JepVariable(String variable) {
		super();
		this.variableName = variable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.data.Data#getData()
	 */
	@Override
	public String getData() {
		return variableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see evm.dmc.core.data.Data#setData(java.lang.Object)
	 */
	@Override
	public void setData(String data) {
		this.variableName = data;

	}

	private String randomString() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < JepVariable.strlength; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

}
