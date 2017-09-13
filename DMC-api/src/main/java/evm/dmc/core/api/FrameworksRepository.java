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
	 * @return {@code Map<DataLoader_descriptor, Framework_descriptor>}
	 */
	public Map<String, String> getDataLoadersDescriptorsMap();
	
	/**
	 * @return {@code Set<DataLoader_descriptor>}
	 */
//	public Set<String> getDataLoadersDescriptors();

	/**
	 * @return {@code Map<DataSaver_descriptor, Framework_descriptor>}
	 */
	public Map<String, String> getDataSaversDescriptorsMap();
	
	/**
	 * @return {@code Set<DataSaver_descriptor>}
	 */
//	public Set<String> getDataSaversDescriptors();

	public Map<String, String> getFunctionsDescriptionsMap(Set<String> functions);
	
	public String getFunctionDescription(String function);
	
	public Map<String, String> findFunctionByWordMap(String word);
	
//	public Set<String> findFunctionByWord(String word);
	
	public Map<String, String> filterFunctionMap(Map<String, String> funDesc, String word);

}
