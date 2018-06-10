package evm.dmc.rest.dto;

import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MetaDataDto  extends ResourceSupport {
	private Long metaDataId;
	
	private String name;
	
	private String description;
	
	private Map<String, DataAttributeDto> attributes;

}
