package evm.dmc.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Objects;

public class ProjectModel {
	private String typeName;
	private List<AlgorithmModel> algorithms = new ArrayList<>();
	private Properties projectProperties = new Properties();
	private String projectName;
	
	public ProjectModel() {
		super();
	}
	
	public ProjectModel(String typeName, List<AlgorithmModel> algorithms, Properties projectProperties, String projectName){
		this.typeName = typeName;
		this.algorithms =  new ArrayList<>(algorithms);
		this.projectProperties = new Properties(projectProperties);
		this.projectName = projectName;
	}
	
	/**
	 * @return the name
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param name the name to set
	 */
	public void setTypeName(String name) {
		this.typeName = name;
	}

	/**
	 * @return the list of algorithms
	 */
	public List<AlgorithmModel> getAlgorithms() {
		return algorithms;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithms(List<AlgorithmModel> algorithm) {
		this.algorithms = algorithm;
	}

	/**
	 * @return the projectProperties
	 */
	public Properties getProjectProperties() {
		return projectProperties;
	}

	/**
	 * @param projectProperties the projectProperties to set
	 */
	public void setProjectProperties(Properties projectProperties) {
		this.projectProperties = projectProperties;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProjectModel)) {
			return false;
		}
		ProjectModel castOther = (ProjectModel) other;
		return Objects.equals(typeName, castOther.typeName) && Objects.equals(algorithms, castOther.algorithms)
				&& Objects.equals(projectProperties, castOther.projectProperties)
				&& Objects.equals(projectName, castOther.projectName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeName, algorithms, projectProperties, projectName);
	}

	

}
