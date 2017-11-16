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
	public static final String USER_HOME = "/userHome";
	public static final String ADMIN_HOME = "/adminHome";
	public static final String AUTH = "/authenticate";
	public static final String LOGOUT = "/logout";
	
	public static final String ER_ACC_DINIED = "/errors/accessDenied";
	public static final String ER_NOT_FOUND = "/errors/notFound";
	
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
	 * @return the userhome
	 */
	public static String getUserHome() {
		return USER_HOME;
	}
	/**
	 * @return the adminhome
	 */
	public static String getAdminHome() {
		return ADMIN_HOME;
	}
	/**
	 * @return the auth
	 */
	public static String getAuth() {
		return AUTH;
	}
	/**
	 * @return the logout
	 */
	public static String getLogout() {
		return LOGOUT;
	}
	/**
	 * @return the erAccDinied
	 */
	public static String getErAccDinied() {
		return ER_ACC_DINIED;
	}
	/**
	 * @return the erNotFound
	 */
	public static String getErNotFound() {
		return ER_NOT_FOUND;
	}
	
	
	

}
