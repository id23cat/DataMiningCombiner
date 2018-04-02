package evm.dmc.web.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
@ConfigurationProperties("views")
@lombok.Data
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
	@Getter public final Project project = new Project();
			
	@lombok.Data
	public static class Fragments {
		private String signin;
		private String regsign;
	}
	
	@lombok.Data
	public static class Errors {
		private String accessDenied;
		private String notFound;
		private String userExists;
	}
	
	@lombok.Data
	public static class Project {
		private String main;
		private String algorithmsList;
		private String wizardBase;
		private String datasourcesList;
		
		@Getter public final Data data = new Data();
		@Getter public final Algorithm algorithm = new Algorithm();
//		@Getter public final Wizard wizard = new Wizard();
		
		@lombok.Data
		public static class Data {
			public String dataSource;
		}
		
		@lombok.Data
		public static class Algorithm {
			public String algorithm;
		}
		
//		@lombok.Data
//		public static class Wizard {
//			public String datasource;
//		}
	}

}
