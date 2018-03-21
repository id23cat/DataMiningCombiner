package evm.dmc.api.model.algorithm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import evm.dmc.api.model.FunctionModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Entity
@DiscriminatorValue("fw")
public class FWMethod extends Method {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2208544525075109660L;
	
	private FunctionModel functionModel;

//	@Override
//	public boolean isFunction() {
//		return true;
//	}
//
//	@Override
//	public boolean isSubAlgorithm() {
//		return false;
//	}
	
//	@Override
//	public String getDescription() {
//		return functionModel.getDescription();
//	}
//	
//	@Override
//	public String getName() {
//		return functionModel.getName();
//	}
//
//	@Override
//	public boolean addStep(Method alg) {
//		return false;
//	}
//
//	@Override
//	public boolean delStep(Method alg) {
//		return false;
//	}
//
//	@Override
//	public Optional<FunctionModel> getFunction() {
//		return Optional.of(functionModel);
//	}
//
//	@Override
//	public List<Method> getAlgorithmSteps() {
//		return Collections.emptyList();
//	}


}
