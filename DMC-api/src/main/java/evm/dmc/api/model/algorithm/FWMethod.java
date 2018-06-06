package evm.dmc.api.model.algorithm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import evm.dmc.api.model.FunctionModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Entity
//@NoArgsConstructor
@DiscriminatorValue("fwMethod")
public class FWMethod extends PatternMethod {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2208544525075109660L;
	
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, optional = true)
	private FunctionModel frameworkFunction;
	
	

	@Builder
	private FWMethod(Long id, 
			String name, 
			String description, 
			@Singular Map<String, String> properties,
			List<PatternMethod> steps,
			@Singular Set<Algorithm> dependentAlgorithms, 
			Boolean shared,
			FunctionModel frameworkFunction
			) {
		super(id, name, description, properties, steps, dependentAlgorithms, shared);
//		super(id, name, description, dependentAlgorithms, shared);
		this.frameworkFunction = frameworkFunction;
	}
}
