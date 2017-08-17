package evm.dmc.core;

import java.util.Arrays;
import java.util.HashSet;
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

	public Framework getFramework(String descriptor) {
		return applicationContext.getBean(descriptor, frameworkClass);
	}
	

}
