package evm.dmc.core;


/**
 * The Interface Algorithm.
 */
public interface Algorithm {
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param command the command
	 */
	void addCommand(Command command);
	
	/**
	 * Deletes command from chain of algorithm
	 *
	 * @param command the command
	 */
	
	void delCommand(Command command);
	
	/**
	 * Execute chain of commands.
	 */
	void execute();

}
