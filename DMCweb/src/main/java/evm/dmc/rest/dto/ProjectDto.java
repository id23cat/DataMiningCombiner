package evm.dmc.rest.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.api.model.ProjectType;
import evm.dmc.api.model.account.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectDto extends ResourceSupport {
    private Long projectId;

    private ProjectType projectType;

    private String name;

    private Timestamp created;
}
