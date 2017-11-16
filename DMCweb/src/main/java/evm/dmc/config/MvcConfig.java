package evm.dmc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import evm.dmc.service.RequestPath;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${views.about}")
	String aboutView;
	
	@Value("${views.index}")
	String indexView;
	
	@Value("${views.userHome}")
	String userHome;
	
	@Value("${views.adminHome}")
	String adminHome;
	
	@Value("${views.errors.accessDenied}")
	String erAccesDen;
	
	@Value("${views.errors.notFound}")
	String erNotFound;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(RequestPath.HOME).setViewName(indexView);
        registry.addViewController(RequestPath.ROOT).setViewName(indexView);
        registry.addViewController(RequestPath.ABOUT).setViewName(aboutView);
//        registry.addViewController("/login").setViewName("login");
        registry.addViewController(RequestPath.USER_HOME).setViewName(userHome);
        registry.addViewController(RequestPath.ADMIN_HOME).setViewName(adminHome);
        
        // Errors pages
        registry.addViewController(RequestPath.ER_ACC_DINIED).setViewName(erAccesDen);
        registry.addViewController(RequestPath.ER_NOT_FOUND).setViewName(erNotFound);
        
    }
	
}
