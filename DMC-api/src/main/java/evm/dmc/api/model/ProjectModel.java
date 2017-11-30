package evm.dmc.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;

import evm.dmc.api.model.account.Account;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="PROJECT")
@Data
public class ProjectModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5045386144743151365L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	private ProjectType type;
	
	@Transient
	private Set<AlgorithmModel> algorithms = new HashSet<>();
	
	private Properties projectProperties = new Properties();
	private String projectName;
	
	public ProjectModel() {
		super();
	}
	
	public ProjectModel(ProjectType type, Set<AlgorithmModel> algorithms, Properties projectProperties, String projectName){
		this.type = type;
		this.algorithms =  new HashSet<>(algorithms);
		this.projectProperties = new Properties(projectProperties);
		this.projectName = projectName;
	}
	

}
