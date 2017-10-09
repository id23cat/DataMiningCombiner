package evm.dmc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import evm.dmc.DmcWebApplication;
import evm.dmc.web.HomeController;

@Configuration
//@PropertySource("classpath:views.properties")
@ConfigurationProperties(prefix = "views")
public class ViewsConfig {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	

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
