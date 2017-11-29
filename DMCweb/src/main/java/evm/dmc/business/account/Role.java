package evm.dmc.business.account;

import java.io.Serializable;


public enum Role implements Serializable {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	private final String name;
	
	public static Role getRole(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for Role");
        }
        if (name.toUpperCase().equals("ROLE_ADMIN")) {
            return ADMIN;
        } else if (name.toUpperCase().equals("ROLE_USER")) {
            return USER;
        } 
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any Role");
    }
	
	private Role(final String role) {
		this.name = role;
	}

	public final String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
