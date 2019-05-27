package evm.dmc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Views views;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(RequestPath.about).setViewName(views.getAbout());
        registry.addViewController(RequestPath.userHome).setViewName(views.getUserHome());
        registry.addViewController(RequestPath.adminHome).setViewName(views.getAdminHome());

        // Errors pages
        registry.addViewController(RequestPath.erAccDenied).setViewName(views.getErrors().getAccessDenied());
        registry.addViewController(RequestPath.erNotFound).setViewName(views.getErrors().getNotFound());

        // Test page
        registry.addViewController("/test").setViewName("test");
        registry.addViewController("/datasource").setViewName("testing/algorithm/datasource");
        registry.addViewController("/datapreview").setViewName("testing/algorithm/function_datapreview");
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
