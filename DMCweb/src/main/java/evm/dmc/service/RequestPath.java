package evm.dmc.service;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public  final  class RequestPath {
	private RequestPath(){};
	@Getter public static final String root = "/";
	@Getter public static final String home = "/home";
	@Getter public static final String about = "/about";
	@Getter public static final String register = "/register";
	@Getter public static final String signin = "/signin";
	@Getter public static final String userHome = "/userHome";
	@Getter public static final String adminHome = "/adminHome";
	@Getter public static final String auth = "/authenticate";
	@Getter public static final String logout = "/logout";
	
	@Getter public static final String erAccDenied = "/errors/accessDenied";
	@Getter public static final String erNotFound = "/errors/notFound";
	
}
