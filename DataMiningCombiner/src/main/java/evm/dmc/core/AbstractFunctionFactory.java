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
	
	public void addFramework(Framework framework){
		this.frameworks.add(framework);
	}
	
	protected void buildFunctionsMap(){
		for(Framework fwk: frameworks){
			List<String> functions = fwk.getFunctionDescriptors();
			for(String descr: functions){
				functionsMap.put(descr, fwk);
			}
		}
	}

}
