package evm.dmc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import evm.dmc.config.ApplicationConfiguration;

//for jsr310 java 8 java.time.*
@EntityScan(
      basePackageClasses = {DmcWebApplication.class, Jsr310JpaConverters.class}
)

@SpringBootApplication
@Import(ApplicationConfiguration.class)
public class DmcWebApplication extends SpringBootServletInitializer {
	
//	@Autowired
//    Environment environment;
	
//	@Value("${spring.profiles.active}")
//	private String activeProfile;

	public static void main(String[] args) {
		SpringApplication.run(DmcWebApplication.class, args);
	}

	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DmcWebApplication.class);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			for (String beanName : beanNames) {
//				System.out.println(beanName);
//			}
//			
//			for (final String profileName : environment.getActiveProfiles()) {
//				System.out.println("Currently active profile - " + profileName);
//			}
//			
//			for (final String profileName : environment.getDefaultProfiles()) {
//				System.out.println("Currently default profile - " + profileName);
//			}
//		};
//	}
	
}
