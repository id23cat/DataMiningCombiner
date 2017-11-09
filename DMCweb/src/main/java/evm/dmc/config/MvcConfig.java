package evm.dmc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import evm.dmc.service.RequestPath;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${views.about}")
	String aboutView;
	
	@Value("${views.index}")
	String indexView;
	
	@Value("${views.regsign}")
	String regSignView;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(RequestPath.HOME).setViewName(indexView);
        registry.addViewController(RequestPath.ROOT).setViewName(indexView);
        registry.addViewController(RequestPath.ABOUT).setViewName(aboutView);
//        registry.addViewController(RequestPath.REGSIGN).setViewName(regSignView);
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/login").setViewName("login");
    }
}
