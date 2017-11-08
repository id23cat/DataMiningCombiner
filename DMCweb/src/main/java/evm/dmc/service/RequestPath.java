package evm.dmc.service;

import org.springframework.stereotype.Service;

@Service
public  final  class RequestPath {
	private RequestPath(){};
	public static final String ROOT = "/";
	public static final String HOME = "/home";
	public static final String ABOUT = "/about";
	public static final String REGISTER = "/register";
	public static final String SIGNIN = "/signin";
	public static final String AUTH = "/authenticate";
	/**
	 * @return the root
	 */
	public static String getRoot() {
		return ROOT;
	}
	/**
	 * @return the home
	 */
	public static String getHome() {
		return HOME;
	}
	/**
	 * @return the about
	 */
	public static String getAbout() {
		return ABOUT;
	}
	/**
	 * @return the register
	 */
	public static String getRegister() {
		return REGISTER;
	}
	/**
	 * @return the signin
	 */
	public static String getSignin() {
		return SIGNIN;
	}
	/**
	 * @return the auth
	 */
	public static String getAuth() {
		return AUTH;
	}
	

}
