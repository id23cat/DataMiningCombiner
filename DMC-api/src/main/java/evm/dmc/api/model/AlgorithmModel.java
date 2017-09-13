package evm.dmc.api.model;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmModel {
	String name;
	FunctionSrcModel dataSource = null;
	List<FunctionModel> functions = new LinkedList<>();
	FunctionDstModel dataDestination = null;
	
	public AlgorithmModel () {}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dataSource
	 */
	public FunctionSrcModel getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(FunctionSrcModel dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the functions
	 */
	public List<FunctionModel> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<FunctionModel> functions) {
		this.functions = functions;
	}
	
	public void addFunction(FunctionModel func) {
		this.functions.add(func);
	}
	
	public void delFunction(FunctionModel func) {
		this.functions.remove(func);
	}

	/**
	 * @return the dataDestination
	 */
	public FunctionDstModel getDataDestination() {
		return dataDestination;
	}

	/**
	 * @param dataDestination the dataDestination to set
	 */
	public void setDataDestination(FunctionDstModel dataDestination) {
		this.dataDestination = dataDestination;
	};
	

}
