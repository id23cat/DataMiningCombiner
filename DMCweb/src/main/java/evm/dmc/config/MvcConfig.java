package evm.dmc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	Views views;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController(RequestPath.home).setViewName(views.getIndex());
//        registry.addViewController(RequestPath.root).setViewName(views.getIndex());
        registry.addViewController(RequestPath.about).setViewName(views.getAbout());
//        registry.addViewController("/login").setViewName("login");
        registry.addViewController(RequestPath.userHome).setViewName(views.getUserHome());
        registry.addViewController(RequestPath.adminHome).setViewName(views.getAdminHome());
        
        // Test page
        registry.addViewController("/test").setViewName("test");
        
        // Errors pages
        registry.addViewController(RequestPath.erAccDenied).setViewName(views.getErrors().getAccessDenied());
        registry.addViewController(RequestPath.erNotFound).setViewName(views.getErrors().getNotFound());
        
    }
	
	// Validation & messages
	// https://teamtreehouse.com/library/displaying-validation-messages
	@Bean
	  public MessageSource messageSource() {
	      ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
	      bean.setBasename("classpath:messages");
	      bean.setDefaultEncoding("UTF-8");
	      return bean;
	  }

	  @Bean
	  public LocalValidatorFactoryBean validator() {
	      LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	      bean.setValidationMessageSource(messageSource());
	      return bean;
	  }

	  @Override
	  public Validator getValidator() {
	      return validator();
	  }
	
}
