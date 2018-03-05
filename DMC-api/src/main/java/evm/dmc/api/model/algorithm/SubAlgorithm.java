package evm.dmc.api.model.algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.ProjectModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true, exclude={"algorithmSteps"})
@ToString(callSuper=true, exclude={"algorithmSteps"})
@Entity
@DiscriminatorValue("subalg")
public class SubAlgorithm  extends Algorithm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5198834992261825155L;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "alg_subalg", joinColumns = @JoinColumn(name="alg_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name="subalg_id", referencedColumnName="id"))
	private final List<Algorithm> algorithmSteps = new LinkedList<>();

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isSubAlgorithm() {
		return true;
	}
	
	@Override
	public boolean addStep(Algorithm alg) {
		return algorithmSteps.add(alg);
	}
	
	@Override
	public boolean delStep(Algorithm alg) {
		return algorithmSteps.remove(alg);
	}

	@Override
	public Optional<FunctionModel> getFunction() {
		return Optional.ofNullable(null);
	}
	
}
