package evm.dmc.api.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PROJECT"
	,uniqueConstraints={@UniqueConstraint(columnNames = {"account_id", "name"})}
)
@EqualsAndHashCode(exclude={"algorithms", "dataSources"})
@ToString(exclude={"algorithms", "dataSources"})
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
	
//	@Version
//	private Long version;
	
	@ManyToOne(cascade = CascadeType.MERGE)	//(optional = false)
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Builder.Default 
	private ProjectType projectType = ProjectType.SIMPLEST_PROJECT;
	
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "project_algorithm", 
//			joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(name = "algorithm_id", referencedColumnName = "id")
//	)
	@OneToMany(mappedBy="project", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
//	@Singular
	@Builder.Default
	private Set<Algorithm> algorithms = new HashSet<>();
	
	
//	@ElementCollection
//	@MapKeyColumn(name = "property")
//	@Column(name = "value")
//	@CollectionTable(name = "projectProps")
//	@Singular
//	private Map<String, String> properties;
	
	@NotBlank(message = ProjectModel.NOT_BLANK_MESSAGE)
	@Column(nullable = false)
	private String name;
	
//	@CreatedDate
	@CreationTimestamp
	@Column(insertable = true, updatable = false)
	@Setter(AccessLevel.NONE) 
//	@Builder.Default
//	private Instant created = Instant.now();
	private java.sql.Timestamp created;
	
	@OneToMany(mappedBy = "project" ,cascade = CascadeType.ALL, orphanRemoval = true)
	@Singular
	private Set<MetaData> dataSources;
	
	
//	public Properties getProperties() {
//		Properties props = new Properties();
//		props.putAll(properties);
//		return props;
//	}
//	
//	public void setProperties(Properties prop) {
////		prop.stringPropertyNames().parallelStream().map((name)->propMap.put(name, prop.getProperty(name)));
////		propMap.prop.entrySet()
//		for(String name: prop.stringPropertyNames()) {
//			properties.put(name, prop.getProperty(name));
//		}
//	}
	
	public synchronized void addMetaData(MetaData meta) {
		meta.setProject(this);
		this.dataSources.add(meta);
	}

}
