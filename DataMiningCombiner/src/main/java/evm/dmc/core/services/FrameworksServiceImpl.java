package evm.dmc.core.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.core.AbstractFramework;
import evm.dmc.core.api.DMCDataLoader;
import evm.dmc.core.api.DMCDataSaver;
import evm.dmc.core.api.DMCFunction;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.exceptions.NoSuchFunctionException;
import evm.dmc.core.services.repositories.FrameworkModelCoreRepository;
import evm.dmc.core.services.repositories.FunctionModelCoreRepository;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FrameworksServiceImpl implements FrameworksService {
	
	private ApplicationContext applicationContext;
	private static Class<AbstractFramework> frameworkClass = AbstractFramework.class;
	
	private FrameworkModelCoreRepository frameRepo;
	
	private Set<String> frmwkIDs = new HashSet<String>();

	public FrameworksServiceImpl(@Autowired FrameworkModelCoreRepository frameRepo) {
		this.frameRepo = frameRepo;
	}
	
	@PostConstruct
	protected void postInit() {
		frmwkIDs.addAll(Arrays.asList(applicationContext.getBeanNamesForType(frameworkClass)));
//		frameRepo.save(getFramework(frmwkIDs))
		frmwkIDs.stream().forEach(
				(String id) -> {
					FrameworkModel model = getFramework(id).getFrameworkModel();
					model.setActive(true); 		// setting current framework as active
					frameRepo.save(model);
				});
	}
	
	@Override
	public Set<String> getFrameworksDescriptors() {
		return frmwkIDs;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	@Override
	public Framework getFramework(String descriptor) {
		return applicationContext.getBean(descriptor, frameworkClass);
	}
	
	/**
	 * @return Map<Framework_descriptor, Set<Framework's_functions>>
	 */
	@Override
	public Map<String, String> getFunctionsDescriptors() {
		Map<String, String> functions = new HashMap<>();
		for (String framework : getFrameworksDescriptors()) {
			for(String function : getFramework(framework).getFunctionDescriptors())
				functions.put(function, framework);
		}
		return functions;
	} 

	@Override
	public DMCFunction<?> getFunction(String descriptor) throws NoSuchFunctionException {
		Map<String, String> funcMap = getFunctionsDescriptors();
		
		return getFunction(descriptor, funcMap.get(descriptor));
	}
	
	@Override
	public DMCFunction<?> getFunction(FunctionModel model) throws NoSuchFunctionException {
		return getFunction(model.getName(), model.getFramework().getName()).setFunctionModel(model);
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
	
	
	
	
	
	private <T> T getFunction(String descriptor, Framework framework, Class<T> type) {
		return framework.getDMCFunction(descriptor, type);
	}

}
