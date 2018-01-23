package evm.dmc.web.service;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public  final  class RequestPath {
	private RequestPath(){};
	@Getter public static final String root = "/";
	@Getter public static final String home = root;
	@Getter public static final String about = "/about";
	@Getter public static final String register = "/register";
	@Getter public static final String signin = "/signin";
	@Getter public static final String userHome = "/userHome";
	@Getter public static final String adminHome = "/adminHome";
	@Getter public static final String auth = "/authenticate";
	@Getter public static final String logout = "/logout";
	
	@Getter public static final String project ="/project";
	@Getter public static final String add ="/add";
	@Getter public static final String delete="/delete";
	@Getter public static final String addProject = project + add;
	@Getter public static final String delProject = project + delete;
//	@Getter public static final String newAlg = project + "/newalg";
	
	@Getter public static final String algorithm = "/algorithm";
	
	@Getter public static final String delAlgorithm = algorithm + delete;
	@Getter public static final String projDelAlgorithm = project + delAlgorithm;
	@Getter public static final String addAlgorithm = algorithm + add;
	@Getter public static final String projAddAlgorithm = project + addAlgorithm;
	
	@Getter public static final String setSource = "/setsrc";
	
	@Getter public static final String algorithmWozard = "/project/algorithm";
	
	
	@Getter public static final String erAccDenied = "/errors/accessDenied";
	@Getter public static final String erNotFound = "/errors/notFound";
	
}
