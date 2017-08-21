package evm.dmc.core.api;

import java.util.Map;
import java.util.Set;


import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import evm.dmc.core.api.Framework;

@Service
public interface FrameworksRepository extends ApplicationContextAware {
	
	public Set<String> getFrameworksDescriptors();
	
	public Framework getFramework(String descriptor);
	
	public DMCFunction getFunction(String descriptor);
	
	public DMCFunction getFunction(String descriptor, String framework);
	
	public DMCFunction getFunction(String descriptor, Framework framework);

	/**
	 * @return {@code Map<Framework's_functions, Framework_descriptor>}
	 */
	public Map<String, String> getFunctionsDescriptors();

	/**
	 * @return {@code Map<Framework's_DataLoaders, Framework_descriptor>}
	 */
	public Map<String, String> getDataLoadersDescriptors();

	/**
	 * @return {@code Map<Framework's_DataSavers, Framework_descriptor>}
	 */
	public Map<String, String> getDataSaversDescriptors();

	public Map<String, String> getFunctionsDescriptions(Set<String> functions);
	
	public String getFunctionDescription(String function);
	
	public Map<String, String> findFunctionByWord(String word);
	
	public Map<String, String> filterFunction(Map<String, String> funDesc, String word);

}
