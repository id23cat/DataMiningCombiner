package evm.dmc.webApi.dto;

import org.springframework.hateoas.ResourceSupport;

public abstract class AbstractDto extends ResourceSupport {
	public abstract Long getDtoId();
}
