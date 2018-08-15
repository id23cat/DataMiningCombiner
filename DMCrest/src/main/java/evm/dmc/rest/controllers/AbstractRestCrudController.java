package evm.dmc.rest.controllers;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.rest.controllers.interfaces.RestCrudController;
import evm.dmc.webApi.dto.AbstractDto;

public abstract class AbstractRestCrudController<T extends AbstractDto> 
	implements RestCrudController<T> {

}
