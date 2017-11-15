package evm.dmc.business.account;

import javax.persistence.Entity;

@Entity
public class AccountExt extends Account{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7054355736554325207L;

	public AccountExt(Account acc) {
		super(acc);
	}
	
	public AccountExt(Account acc, String role){
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

	public void setRole(String role) {
		super.role = role;
	}
	
	public Account getAccount() {
		return this;
	}
}
