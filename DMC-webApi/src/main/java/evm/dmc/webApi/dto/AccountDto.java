package evm.dmc.webApi.dto;

import java.time.Instant;
import java.util.Set;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountDto extends ResourceSupport {
	
//	@Setter
	private Long accountId;
	
	private String userName;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	protected Role role;
	
	private Instant created;
}
