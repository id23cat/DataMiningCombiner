package evm.dmc.api.model;

import java.util.List;

public class AlgorithmModel {
	String name;
	DataSrcDstModel dataSource;
	List<FunctionModel> functions;
	DataSrcDstModel dataDestination;
	
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
	public DataSrcDstModel getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSrcDstModel dataSource) {
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
	public DataSrcDstModel getDataDestination() {
		return dataDestination;
	}

	/**
	 * @param dataDestination the dataDestination to set
	 */
	public void setDataDestination(DataSrcDstModel dataDestination) {
		this.dataDestination = dataDestination;
	};
	

}
