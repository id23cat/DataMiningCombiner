package evm.dmc.api.model.account;

import java.time.Instant;

import javax.persistence.Entity;

@Entity
public class AccountExt extends Account{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7054355736554325207L;
	
	protected AccountExt() {}

	public AccountExt(Account acc) {
		super(acc);
	}
	
	public AccountExt(Account acc, String role){
		super(acc);
		this.setRole(role);
	}
	
	public AccountExt(Account acc, Role role){
		super(acc);
		this.setRole(role);
	}
	
	public AccountExt(String username, String password, String email, 
			String firstName, String lastName) {
		super(username, password, email, firstName, lastName);
	}
	
	public AccountExt(String username, String password, String email, 
			String firstName, String lastName, String role) {
		super(username, password, email, firstName, lastName);
		this.setRole(role);
	}
	
	public AccountExt(String username, String password, String email, 
			String firstName, String lastName, Role role) {
		super(username, password, email, firstName, lastName);
		super.setRole(role);
	}

	public void setRole(String role) {
		super.role = Role.valueOf(role);
	}
	
//	public void setRole(Role role) {
//		super.role = role;
//	}
	
	public Account getAccount() {
		return this;
	}
}
