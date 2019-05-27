package evm.dmc.api.model.account;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class AccountExt extends Account {

    private static final long serialVersionUID = 7054355736554325207L;

    public AccountExt(String username, String password, String email,
                      String firstName, String lastName) {
        super(username, password, email, firstName, lastName);
        this.setRole(role);
    }

    public AccountExt(String username, String password, String email,
                      String firstName, String lastName, Role role) {
        super(username, password, email, firstName, lastName, role);
    }

    public void setRole(String role) {
        super.role = Role.valueOf(role);
    }

}
