package evm.dmc.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FrameworkModel {
	private String name;
	private List<FunctionModel> functions = new ArrayList<>();;
	private FrameworkType type;
	private Properties properties = new Properties();
	
	public FrameworkModel() {}
	
	public FrameworkModel(String name, FrameworkType type) {
		super();
		this.name = name;
		this.type = type;
	}

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

	/**
	 * @return the type
	 */
	public FrameworkType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(FrameworkType type) {
		this.type = type;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functions == null) ? 0 : functions.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FrameworkModel other = (FrameworkModel) obj;
		if (functions == null) {
			if (other.functions != null)
				return false;
		} else if (!functions.equals(other.functions))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
