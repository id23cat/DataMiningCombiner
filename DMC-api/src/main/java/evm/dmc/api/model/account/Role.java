package evm.dmc.api.model.account;

import java.io.Serializable;


public enum Role implements Serializable {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	private final String name;
	
	public static Role getRole(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for Role");
        }
        if (name.toUpperCase().contains("ADMIN")) {
            return ADMIN;
        } else if (name.toUpperCase().contains("USER")) {
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
	
	public final String getRole() {
		return this.toString();
	}
	
	public final String getAutority() {
		return getName();
	}

	/*@Override
	public String toString() {
		return getName();
	}*/
	
}
