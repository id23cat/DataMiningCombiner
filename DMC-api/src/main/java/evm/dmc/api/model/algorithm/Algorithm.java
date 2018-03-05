package evm.dmc.api.model.algorithm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.listeners.AlgorithmEntityListener;
import evm.dmc.api.model.data.MetaData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude="dependentProjects")
@ToString(exclude="dependentProjects")
@Entity
@EntityListeners(AlgorithmEntityListener.class)
@Table(name="ALGORITHM"
//	,uniqueConstraints={@UniqueConstraint(columnNames = {"parentProject_id", "name"})}
)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Algorithm implements Serializable {
	
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
	@JoinColumn(name = "parent_project_id")
	@NotNull
//	@Column(nullable = false)
	private ProjectModel parentProject = null;
	
	@ManyToMany(mappedBy = "algorithms", cascade = {CascadeType.ALL})
	private Set<ProjectModel> dependentProjects = new HashSet<>();
	
	@Column(columnDefinition="boolean default false")
	private boolean shared = false;
	
	// null -- means getting source form previous function in hierarchy
	@Column(nullable = true)
	private MetaData dataSource = null;
	
	// null -- means redirect result to next function in hierarchy
	@Column(nullable = true)
	private MetaData dataDestination = null;
	
	
	public abstract boolean isFunction();
	
	public abstract boolean isSubAlgorithm();
	
	public abstract Optional<FunctionModel> getFunction();
	
	public abstract List<Algorithm> getAlgorithmSteps();
	
	public abstract boolean addStep(Algorithm alg);
	
	public abstract boolean delStep(Algorithm alg);
	

//	
//	
//	// Unidirectional One-to-many association
//	@OneToMany
//	@JoinColumn(name = "function_id")
//	@OrderColumn(name = "functions_order")
//	private List<FunctionModel> functions = new LinkedList<>();
//	
//	private FunctionDstModel dataDestination = null;
//	

//	
//	public AlgorithmModel addFunction(FunctionModel func) {
//		this.functions.add(func);
//		return this;
//	}
//	
//	public AlgorithmModel delFunction(FunctionModel func) {
//		this.functions.remove(func);
//		return this;
//	}
//	
//	public FunctionModel newFunction() {
//		return new FunctionModel();
//	}
//	
//	public FunctionSrcModel newSource() {
//		return new FunctionSrcModel();
//	}
//	
//	public FunctionDstModel newDestination() {
//		return new FunctionDstModel();
//	}

}
