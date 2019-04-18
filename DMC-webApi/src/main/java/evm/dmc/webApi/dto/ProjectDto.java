package evm.dmc.webApi.dto;

import java.sql.Timestamp;

import evm.dmc.api.model.ProjectType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for Project model
 *
 * @see evm.dmc.api.model.ProjectModel
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
