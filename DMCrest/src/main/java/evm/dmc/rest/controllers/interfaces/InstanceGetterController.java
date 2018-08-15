package evm.dmc.rest.controllers.interfaces;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import evm.dmc.webApi.dto.AbstractDto;

public interface InstanceGetterController<T extends AbstractDto> {
	T getInstance(Long accountId, Long id);
	List<T> getInstancesList(Long accountId);
	
	String getListRelatioinName();
	
	default T selfLink(T dto, Long accountId) {
		dto.add(linkTo(methodOn(this.getClass())
				.getInstance(accountId, dto.getDtoId()))
				.withSelfRel());
		return dto;
	}
	
	default T listLink(T dto, Long accountId) {
		dto.add(linkTo(methodOn(this.getClass())
				.getInstancesList(accountId))
			.withRel(getListRelatioinName()));
		return dto;
	}

}
