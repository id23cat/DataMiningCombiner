package evm.dmc.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Service
@ConfigurationProperties("views")
@Data
public class Views {
	private String index;
	private String about;
	private String register;
	private String signin;
	private String regsign;
	private String userHome;
	private String adminHome;
	
	@Getter private final Fragments fragments = new Fragments();
	@Getter private final Errors errors = new Errors();
	@Getter private final Project project = new Project();
			
	@Data
	public static class Fragments {
		private String signin;
		private String regsign;
	}
	
	@Data
	public static class Errors {
		private String accessDenied;
		private String notFound;
		private String userExists;
	}
	
	@Data
	public static class Project {
		private String main;
		private String algorithmsList;
		private String newAlg;
	}

}
