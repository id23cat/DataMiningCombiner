package evm.dmc.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name="Algorithm")
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
	
	@ManyToOne
	@JoinColumn(name = "function_id")
	@OrderColumn(name = "functions_order")
	private List<FunctionModel> functions = new LinkedList<>();
	
	private FunctionDstModel dataDestination = null;
	
	@ManyToMany(mappedBy = "algorithms")
	private ProjectModel parentProject;

	private boolean shared = false;
	
	public void addFunction(FunctionModel func) {
		this.functions.add(func);
	}
	
	public void delFunction(FunctionModel func) {
		this.functions.remove(func);
	}

}
