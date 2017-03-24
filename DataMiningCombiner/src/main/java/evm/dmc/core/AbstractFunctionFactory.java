package evm.dmc.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class  AbstractFunctionFactory {
	protected Map <String, Framework> functionsMap = new HashMap<>();
	@Autowired protected List<Framework> frameworks; 
	
	public AbstractFunctionFactory(){};
	
	public void setFrameworks(List<Framework> fwList){
		this.frameworks	= fwList;
	}

}
