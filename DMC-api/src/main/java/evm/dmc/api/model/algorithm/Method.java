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
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.OneToMany;
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
//@EqualsAndHashCode(exclude="dependentProjects")
//@ToString(exclude="dependentProjects")
@Entity
//@EntityListeners(AlgorithmEntityListener.class)
@Table(name="Method"
//	,uniqueConstraints={@UniqueConstraint(columnNames = {"parentProject_id", "name"})}
)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("method")
public abstract class Method implements Serializable {
	
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
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "alg_subalg", joinColumns = @JoinColumn(name="alg_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name="subalg_id", referencedColumnName="id"))
	private final List<Method> steps = new LinkedList<>();
	
	@OneToMany(mappedBy = "method", cascade = {CascadeType.ALL})
	private Set<Algorithm> dependentAlgorithms = new HashSet<>();
	
	@Column(columnDefinition="boolean default false")
	private boolean shared = false;
	
	// null -- means getting source form previous function in hierarchy
	@Column(nullable = true)
	private MetaData dataSource = null;
	
	
	
	// null -- means redirect result to next function in hierarchy
	@Column(nullable = true)
	private MetaData dataDestination = null;
	
//	
//	public abstract boolean isFunction();
//	
//	public abstract boolean isSubAlgorithm();
//	
//	public abstract Optional<FunctionModel> getFunction();
//	
//	public abstract List<Method> getAlgorithmSteps();
//	
//	public abstract boolean addStep(Method alg);
//	
//	public abstract boolean delStep(Method alg);
	

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
