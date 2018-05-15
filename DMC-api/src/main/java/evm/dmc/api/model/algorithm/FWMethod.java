package evm.dmc.api.model.algorithm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.converters.PropertiesMapToJson;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Entity
@DiscriminatorValue("fw")
public class FWMethod extends PatternMethod {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2208544525075109660L;
	
	private FunctionModel frameworkFunction;
	
	@Column( length = 100000 )
	@Convert(converter = PropertiesMapToJson.class)
	private Map<String,String> overridenProperties;

	@Builder
	private FWMethod(Long id, 
			String name, 
			String description, 
			List<PatternMethod> steps, 
			Set<Algorithm> dependentAlgorithms, 
			Boolean shared,
			FunctionModel frameworkFunction,
			@Singular Map<String,String> properties) {
		super(id, name, description, steps, dependentAlgorithms, shared);
//		super(id, name, description, dependentAlgorithms, shared);
		this.frameworkFunction = frameworkFunction;
		this.overridenProperties = properties;
	}
}
