package evm.dmc.api.model.algorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.listeners.AlgorithmEntityListener;
import evm.dmc.api.model.converters.MapAttributesToJson;
import evm.dmc.api.model.converters.OptionalMapAttributesToJson;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(exclude={"srcAttributes"})
@Entity
//@EntityListeners(AlgorithmEntityListener.class)
@Table(name="ALGORITHM"
	,uniqueConstraints={@UniqueConstraint(columnNames = {"project_id", "name"})}
)
@Slf4j
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
public class Algorithm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 386821974686652567L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@NotBlank
	@Length(max = 100)
	private String name;
	
	@Length(max=1000)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	@NotNull
//	@Column(nullable = false)
	private ProjectModel project = null;
	
	// null -- means getting source form previous function in hierarchy
//	@Column(nullable = true)
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, optional = true)
	private MetaData dataSource = null;
	
	@Column( length = 100000 )
	@Convert(converter = MapAttributesToJson.class)
	private Map<String, DataAttribute> srcAttributes;

	// null -- means redirect result to next function in hierarchy
	@Column(nullable = true)
	private MetaData dataDestination = null;
	
//	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "function_id")
	private Method method;
	
	public Map<String, DataAttribute> getSrcAttributes() {
		if(dataSource == null)
			return null;
		return Optional.ofNullable(srcAttributes).orElseGet(() -> dataSource.getAttributes());
	}
	
//	public void setDataSource(MetaData meta) {
//		log.debug("-== Setting MetaData to Algorith: {}", meta);
//		dataSource = meta;
//	}
}
