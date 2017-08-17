package evm.dmc.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import evm.dmc.core.function.DMCFunction;
import evm.dmc.weka.function.WekaFunctions;

@Service
@PropertySource("classpath:fwrepo.properties")
public class FrameworksRepository implements ApplicationContextAware {
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
	public Map<String, Set<String>> getFunctionsDescriptors() {
		Map<String, Set<String>> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			functions.put(framework, getFramework(framework).getFunctionDescriptors());
		}
		return functions;
	}

	/**
	 * @return Map<Framework_descriptor, Set<Framework's_DataLoaders>>
	 */
	public Map<String, Set<String>> getDataLoaderssDescriptors() {
		Map<String, Set<String>> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			functions.put(framework, getFramework(framework).getLoaderDescriptors().keySet());
		}
		return functions;
	}

	/**
	 * @return Map<Framework_descriptor, Set<Framework's_DataSavers>>
	 */
	public Map<String, Set<String>> getDataSaversDescriptors() {
		Map<String, Set<String>> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			functions.put(framework, getFramework(framework).getSaverDescriptors().keySet());
		}
		return functions;
	}

	public Map<String, String> getFunctionsDescriptions(Set<String> functions) {
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

}
