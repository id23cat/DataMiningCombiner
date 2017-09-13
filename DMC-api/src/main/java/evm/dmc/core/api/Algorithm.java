package evm.dmc.core.api;

import java.io.IOException;
import java.util.List;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.api.DMCFunction;

/**
 * The Interface Algorithm.
 */
/**
 * @author id23cat
 *
 */
public interface Algorithm {
	
	Algorithm setFrameworksRepository(FrameworksRepository repo);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param dMCFunction the command
	 */
	void addCommand(DMCFunction<?> dMCFunction);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param functionModel -- model of the command
	 */
	void addCommand(FunctionModel functionModel);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param descriptor of the function
	 */
	void addCommand(String descriptor);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param dMCFunction the command
	 */
	void addDataSource(DMCDataLoader dataSource);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param functionModel -- model of the command
	 */
	void addDataSource(FunctionModel functionModel);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param descriptor of the function
	 * @param source string representation of the source (like a file or URL)
	 */
	void addDataSource(String descriptor, String source);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param dMCFunction the command
	 */
	void addDataDestination(DMCDataSaver dataDest);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param functionModel -- model of the command
	 */
	void addDataDestination(FunctionModel functionModel);
	
	/**
	 * Adds the command to the chain of algorithm
	 *
	 * @param descriptor of the function
	 * @param destination string representation of the destination (like a file or URL)
	 */
	void addDataDestination(String descriptor, String destination);
	
	/**
	 * Deletes command from chain of algorithm
	 *
	 * @param dMCFunction the command
	 * @return TODO
	 */
	
	boolean delCommand(DMCFunction<?> dMCFunction);
	
	/**
	 * Deletes command from chain of algorithm
	 *
	 * @param functionModel -- model of the command
	 */
	
	boolean delCommand(FunctionModel functionModel);
	
	/**
	 * Deletes command from chain of algorithm
	 *
	 * @param descriptor of the function
	 * @return TODO
	 */
	
	boolean delCommand(String descriptor);
	
	/**
	 * @param dMCFunction function to insert
	 * @param after which to insert 
	 */
	void insertCommand(DMCFunction<?> dMCFunction, DMCFunction<?> after);
	
	/**
	 * @param functionModel -- model of the command
	 * @param after which to insert 
	 */
	void insertCommand(FunctionModel functionModel, DMCFunction<?> after);
	
	/**
	 * @param descriptor of the function
	 * @param after which to insert 
	 */
	void insertCommand(String descriptor, DMCFunction<?> after);
	
	/**
	 * Execute chain of commands.
	 */
	void execute()  throws IOException;
	
	Algorithm setModel(AlgorithmModel model);
	
	AlgorithmModel getModel();
	
	
	/**
	 * @return unmodifiable List view of functions flow
	 */
	List<DMCFunction<?>> getFunctionsList();

}
