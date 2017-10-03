package evm.dmc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
//@PropertySource("classpath:views.properties")
@ConfigurationProperties(prefix = "views")
public class ViewsConfig {
	
	@Value("${spring.view.prefix}")
	private String prefix = "";

	@Value("${spring.view.suffix}")
	private String suffix = "";

	@Value("${spring.view.view-names}")
	private String viewNames = "";
	/*
	 * Config file to resolve view names
	 */
	private final static String YAML = "views.yml";
	
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
	  yaml.setResources(new ClassPathResource(YAML));
	  propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
	  return propertySourcesPlaceholderConfigurer;
	}
	
	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		final InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix(prefix);
		vr.setSuffix(suffix);
		vr.setViewClass(JstlView.class);
		vr.setViewNames(viewNames);
		return vr;
	}

}
