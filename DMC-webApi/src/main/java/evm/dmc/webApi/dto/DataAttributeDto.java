package evm.dmc.webApi.dto;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.core.api.AttributeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for Data Attribute model
 *
 * @see evm.dmc.core.api.AttributeType
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataAttributeDto extends ResourceSupport {

    private String name;
    private AttributeType type;
    private Double multiplier;
    private Boolean checked;

}
