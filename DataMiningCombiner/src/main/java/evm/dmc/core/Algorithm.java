package evm.dmc.core;


/**
 * The Interface Algorithm.
 */
public interface Algorithm {
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param function the command
	 */
	void addCommand(Function function);
	
	/**
	 * Deletes command from chain of algorithm
	 *
	 * @param function the command
	 */
	
	void delCommand(Function function);
	
	/**
	 * Execute chain of commands.
	 */
	void execute();

}
