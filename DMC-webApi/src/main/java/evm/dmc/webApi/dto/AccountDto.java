package evm.dmc.webApi.dto;

import java.time.Instant;

import evm.dmc.api.model.account.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for Account model
 *
 * @see evm.dmc.api.model.account.Account
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountDto extends AbstractDto {

    private Long accountId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private Instant created;

    protected Role role;

    @Override
    public Long getDtoId() {
        return getAccountId();
    }
}
