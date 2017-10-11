package evm.dmc.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Objects;

public class ProjectModel {
	private ProjectType type;
	private List<AlgorithmModel> algorithms = new ArrayList<>();
	private Properties projectProperties = new Properties();
	private String projectName;
	
	public ProjectModel() {
		super();
	}
	
	public ProjectModel(ProjectType type, List<AlgorithmModel> algorithms, Properties projectProperties, String projectName){
		this.type = type;
		this.algorithms =  new ArrayList<>(algorithms);
		this.projectProperties = new Properties(projectProperties);
		this.projectName = projectName;
	}
	
	/**
	 * @return the name
	 */
	public ProjectType getType() {
		return type;
	}

	/**
	 * @param name the name to set
	 */
	public void setType(ProjectType name) {
		this.type = name;
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
		return Objects.equals(type, castOther.type) && Objects.equals(algorithms, castOther.algorithms)
				&& Objects.equals(projectProperties, castOther.projectProperties)
				&& Objects.equals(projectName, castOther.projectName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, algorithms, projectProperties, projectName);
	}

	

}
