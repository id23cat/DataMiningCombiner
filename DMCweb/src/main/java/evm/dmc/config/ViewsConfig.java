package evm.dmc.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;


@Configuration
@ConfigurationProperties(prefix = "views")
public class ViewsConfig {

	/*
	 * Config file to resolve view names
	 */
	private final static String VIEWS_YAML_CONFIG = "views.yml";
	
	private String createalg;

	/**
	 * @return the createalg
	 */
	public String getCreatealg() {
		return createalg;
	}

	/**
	 * @param createalg the createalg to set
	 */
	public void setCreatealg(String createalg) {
		this.createalg = createalg;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
	  PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
	  YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
	  yaml.setResources(new ClassPathResource(VIEWS_YAML_CONFIG));
	  propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
	  return propertySourcesPlaceholderConfigurer;
	}


}
