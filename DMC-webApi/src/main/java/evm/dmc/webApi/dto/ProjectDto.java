package evm.dmc.webApi.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectDto extends AbstractDto {
	private Long projectId;
	
	private ProjectType projectType;
	
	private String name;
	
	private Timestamp created;

	@Override
	public Long getDtoId() {
		return getProjectId();
	}
}
