package evm.dmc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import evm.dmc.config.FileStorageProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

//for jsr310 java 8 java.time.*
@EntityScan(
	basePackages = {"evm.dmc.api.model"},
    basePackageClasses = {Jsr310JpaConverters.class}
)
@SpringBootApplication
//@ComponentScan(basePackages = {"evm.dmc"},
//		includeFilters = @FilterItype)
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class DmcRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmcRestApplication.class, args);
	}
	
	@Bean
	public ExecutorService executorService() {
		return Executors.newCachedThreadPool();
		// new ThreadPoolExecutor
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
