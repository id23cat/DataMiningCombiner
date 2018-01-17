package evm.dmc.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name="Algorithm"
//	,uniqueConstraints={@UniqueConstraint(columnNames = {"parentProject_id", "name"})}
)
public class AlgorithmModel implements Serializable {
	
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
	
	private FunctionSrcModel dataSource = null;
	
	
	// Unidirectional One-to-many association
	@OneToMany
	@JoinColumn(name = "function_id")
	@OrderColumn(name = "functions_order")
	private List<FunctionModel> functions = new LinkedList<>();
	
	private FunctionDstModel dataDestination = null;
	
	@ManyToMany(mappedBy = "algorithms", cascade = {CascadeType.ALL})
	private Set<ProjectModel> dependentProjects = new HashSet<>();

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_project_id")
//	private ProjectModel parentProject;

	private boolean shared = false;
	
	public AlgorithmModel addFunction(FunctionModel func) {
		this.functions.add(func);
		return this;
	}
	
	public AlgorithmModel delFunction(FunctionModel func) {
		this.functions.remove(func);
		return this;
	}

}
