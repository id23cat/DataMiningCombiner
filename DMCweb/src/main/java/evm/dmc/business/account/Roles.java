package evm.dmc.business.account;

import org.springframework.stereotype.Service;

@Service
public final class Roles {
	private Roles(){};
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	/**
	 * @return the admin
	 */
	public static String getAdmin() {
		return ADMIN;
	}
	/**
	 * @return the user
	 */
	public static String getUser() {
		return USER;
	}
	
}
