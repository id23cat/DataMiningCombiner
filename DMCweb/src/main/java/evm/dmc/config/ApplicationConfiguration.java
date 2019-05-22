package evm.dmc.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spring.SpringIntegration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.ObjectMapper;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FunctionModel;
import evm.dmc.service.testing.Service;
import evm.dmc.service.testing.ServiceProperties;
import evm.dmc.web.service.dto.TreeNodeDTO;

@EnableConfigurationProperties(ServiceProperties.class)
@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfiguration {
    @Autowired
    BeanFactory beanFactory;

    @Bean
    public Service service(ServiceProperties properties) {
        return new Service(properties.getMessage());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
        // new ThreadPoolExecutor
    }
	
//	@Bean
//	public ObjectMapper jsonObjectMapper() {
//		return new ObjectMapper();
//	}

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
