package evm.dmc.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.FrameworksRepository;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@PropertySource("classpath:frameworkrepo.properties")
public class FrameworksRepositoryImpl implements FrameworksRepository {
	private ApplicationContext applicationContext;
	private static Class<AbstractFramework> frameworkClass = AbstractFramework.class;

	private Set<String> frmwkIDs = new HashSet<String>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	protected void postInit() {
		frmwkIDs.addAll(Arrays.asList(applicationContext.getBeanNamesForType(frameworkClass)));
	}

	public Set<String> getFrameworksDescriptors() {
		return frmwkIDs;
	}
	
	// TODO: return proxy for remote access
	public Framework getFramework(String descriptor) {
		return applicationContext.getBean(descriptor, frameworkClass);
	}

	/**
	 * @return Map<Framework_descriptor, Set<Framework's_functions>>
	 */
	public Map<String, String> getFunctionsDescriptors() {
		Map<String, String> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			for(String function : getFramework(framework).getFunctionDescriptors())
				functions.put(function, framework);
		}
		return functions;
	}

	/**
	 * @return Map<Framework_descriptor, Set<Framework's_DataLoaders>>
	 */
	public Map<String, String> getDataLoadersDescriptorsMap() {
		Map<String, String> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			for(String function : getFramework(framework).getLoaderDescriptors().keySet())
				functions.put(function, framework);
		}
		return functions;
	}

	/**
	 * @return Map<Framework_descriptor, Set<Framework's_DataSavers>>
	 */
	public Map<String, String> getDataSaversDescriptorsMap() {
		Map<String, String> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			for(String function : getFramework(framework).getSaverDescriptors().keySet())
				functions.put(function, framework);
		}
		return functions;
	}

	public Map<String, String> getFunctionsDescriptionsMap(Set<String> functions) {
		Map<String, String> descs = new HashMap<>();
		for(String func : functions)
			descs.put(func, getFunctionDescription(func));
		return descs;

	}
	
	public String getFunctionDescription(String function) {
		Iterator<String> iter = frmwkIDs.iterator();
		String description = "";
		if(iter.hasNext())
			description = getFramework(frmwkIDs.iterator().next()).getDMCFunction(function).getDescription();
		return description;
	}

	@Override
	public Map<String, String> findFunctionByWordMap(String word) {
		Map<String, String> functions = getFunctionsDescriptors();
		
		return filterFunctionMap(functions, word);
	}

	@Override
	public Map<String, String> filterFunctionMap(Map<String, String> funDesc, String word) {
		Set<String> keys = funDesc.keySet();
		keys.removeIf(p -> !StringUtils.containsIgnoreCase(p, word));
//		for(String key:keys)
//			funDesc.remove(key);
		return funDesc;
	}

	@Override
	public DMCFunction<?> getFunction(String descriptor) throws NoSuchFunctionException {
		Map<String, String> funcMap = getFunctionsDescriptors();
		
		return getFunction(descriptor, funcMap.get(descriptor));
	}
	
	@Override
	public DMCDataLoader getDataLoaderFunction(String descriptor) throws NoSuchFunctionException {
		Map<String, String> funcMap = getFunctionsDescriptors();
		
		return getFunction(descriptor, getFramework(funcMap.get(descriptor)), DMCDataLoader.class);
	}
	
	@Override
	public DMCDataSaver getDataSaverFunction(String descriptor) throws NoSuchFunctionException {
		Map<String, String> funcMap = getFunctionsDescriptors();
		
		return getFunction(descriptor, getFramework(funcMap.get(descriptor)), DMCDataSaver.class);
	}

	@Override
	public DMCFunction<?> getFunction(String descriptor, String framework) throws NoSuchFunctionException {
		return getFunction(descriptor, getFramework(framework));
	}

	@Override
	public DMCFunction<?> getFunction(String descriptor, Framework framework) throws NoSuchFunctionException {
		return framework.getDMCFunction(descriptor);
	}
	
	public <T> T getFunction(String descriptor, Framework framework, Class<T> type) {
		return framework.getDMCFunction(descriptor, type);
	}

}
