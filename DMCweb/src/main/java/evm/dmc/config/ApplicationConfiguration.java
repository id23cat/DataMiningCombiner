package evm.dmc.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import evm.dmc.service.Service;
import evm.dmc.service.ServiceProperties;

@EnableConfigurationProperties(ServiceProperties.class)
@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfiguration {
	@Bean
    public Service service(ServiceProperties properties) {
        return new Service(properties.getMessage());
    }
	
	
}
