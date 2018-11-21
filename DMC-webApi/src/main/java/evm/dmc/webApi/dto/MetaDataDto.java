package evm.dmc.webApi.dto;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for MetaData model
 * 
 * @see evm.dmc.api.model.data.MetaData
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MetaDataDto extends AbstractDto {

	private Long metaDataId;
	private String name;
	private String description;
	private Map<String, DataAttributeDto> attributes;

	@Override
	public Long getDtoId() {
		return getMetaDataId();
	}
}
