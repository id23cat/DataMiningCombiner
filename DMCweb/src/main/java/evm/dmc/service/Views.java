package evm.dmc.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@ConfigurationProperties("views")
public class Views {
	@Getter @Setter private String index;
	@Getter @Setter private String about;
	@Getter @Setter private String register;
	@Getter @Setter private String signin;
	@Getter @Setter private String regsign;
	@Getter @Setter private String userHome;
	@Getter @Setter private String adminHome;
	
	@Getter private final Fragments fragments = new Fragments();
	@Getter private final Errors errors = new Errors();
			
	public static class Fragments {
		@Getter @Setter private String signin;
		@Getter @Setter private String regsign;
	}
	
	public static class Errors {
		@Getter @Setter private String accessDenied;
		@Getter @Setter private String notFound;
	}

}
