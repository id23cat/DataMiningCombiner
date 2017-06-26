package evm.dmc.api.front;

import java.util.List;

public class Algorithm {
	String name;
	DataSrcDst dataSource;
	List<Function> functions;
	DataSrcDst dataDestination;
	
	public Algorithm () {}

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
	public DataSrcDst getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSrcDst dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the functions
	 */
	public List<Function> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}
	
	public void addFunction(Function func) {
		this.functions.add(func);
	}
	
	public void delFunction(Function func) {
		this.functions.remove(func);
	}

	/**
	 * @return the dataDestination
	 */
	public DataSrcDst getDataDestination() {
		return dataDestination;
	}

	/**
	 * @param dataDestination the dataDestination to set
	 */
	public void setDataDestination(DataSrcDst dataDestination) {
		this.dataDestination = dataDestination;
	};
	

}
