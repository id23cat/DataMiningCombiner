package evm.dmc.api.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;


@Data
@Entity
@Table(name="PROJECT"
	,uniqueConstraints={@UniqueConstraint(columnNames = {"account_id", "name"})}
)
@EqualsAndHashCode(exclude={"algorithms"})
@ToString(exclude="algorithms")
public class ProjectModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5045386144743151365L;
	
	@Transient
	private static final String NOT_BLANK_MESSAGE = "{error.emptyField}";

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)	//(optional = false)
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private ProjectType projectType = ProjectType.SIMPLEST_PROJECT;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "project_algorithm", 
			joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "algorithm_id", referencedColumnName = "id")
	)
//	@OneToMany(mappedBy="parentProject", fetch = FetchType.LAZY,
//			cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AlgorithmModel> algorithms = new HashSet<>();
	
//	@Transient
//	private Properties properties = new Properties();
	
	@ElementCollection
	@MapKeyColumn(name = "property")
	@Column(name = "value")
	@CollectionTable(name = "projectProps")
	private Map<String, String> propertiesMap = new HashMap<>();
	
	@NotBlank(message = ProjectModel.NOT_BLANK_MESSAGE)
	@Column(nullable = false)
	private String name;
	
	@Setter(AccessLevel.NONE) 
	private Instant created = Instant.now();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MetaData> dataSources;
	
	public ProjectModel() {
		super();
	}
	
	public ProjectModel(Account acc, ProjectType type, Set<AlgorithmModel> algorithms, Properties properties, String projectName){
		this.projectType = type;
		if(algorithms != null  && !algorithms.isEmpty())
			this.algorithms =  new HashSet<>(algorithms);
		if(this.propertiesMap != null && properties != null && !properties.isEmpty())
			setProperties(properties);
		this.name = projectName;
		acc.addProject(this);
		this.account = acc;
	}
	
//	public ProjectModel addAlgorithm(AlgorithmModel algorithm){
//		algorithms.add(algorithm);
//		algorithm.getDependentProjects().add(this);
//		return this;
//	}
	
	/**
	 * Differs from addAlgorithm by setting this project as parent
	 * @param algorithm
	 * @return this
	 */
	public AlgorithmModel assignAlgorithm(AlgorithmModel algorithm) {
		algorithms.add(algorithm);
		algorithm.setParentProject(this);
		return algorithm;
	}
	
	public ProjectModel removeAlgorithm(AlgorithmModel algorithm) {
		removeAlgorithmLinkToThis(algorithm);
		algorithms.remove(algorithm);
		return this;
	}
	
	public ProjectModel removeAlgorithmsByNames(String[] names) {
//		for(Iterator<AlgorithmModel> iter = algorithms.iterator(); iter.hasNext();) {
//			
//			if(Arrays.stream(names).anyMatch(iter.alg.getName() :: contains)){
//				removeAlgorithm(alg);
//			}
//		}
		
		algorithms.removeIf( alg -> {
				if(!Arrays.stream(names).anyMatch(alg.getName() :: contains)) {
					return false;
				}
				removeAlgorithmLinkToThis(alg);
				return true;
		});

		return this;
	}
	
	
	private ProjectModel removeAlgorithmLinkToThis(AlgorithmModel algorithm) {
		algorithm.getDependentProjects().remove(this);
		return this;
	}
	
//	@PreRemove
//	public void removeProject() {
//		algorithms.parallelStream().forEach((alg) -> alg.getDependentProjects().remove(this));
//		log.debug("===PreDeleting procedure");
//	}
	
	public Properties getProperties() {
		Properties props = new Properties();
		props.putAll(propertiesMap);
		return props;
	}
	
	public void setProperties(Properties prop) {
//		prop.stringPropertyNames().parallelStream().map((name)->propMap.put(name, prop.getProperty(name)));
//		propMap.prop.entrySet()
		for(String name: prop.stringPropertyNames()) {
			propertiesMap.put(name, prop.getProperty(name));
		}
	}
	
	public synchronized void addMetaData(MetaData meta) {
		meta.setProject(this);
		this.dataSources.add(meta);
	}

}
