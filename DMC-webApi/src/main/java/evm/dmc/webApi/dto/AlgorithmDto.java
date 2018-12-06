package evm.dmc.webApi.dto;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * DTO for Data Algorithm model
 *
 * @see evm.dmc.api.model.algorithm.Algorithm
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AlgorithmDto extends AbstractDto {

    private Long algorithmId;
    private String name;
    private String description;
    private ProjectModel project;
    private MetaData dataSource;
    private Map<String, DataAttribute> srcAttributes;
    private MetaData dataDestination;
    private PatternMethod method;

    @Override
    public Long getDtoId() {
        return algorithmId;
    }
}
