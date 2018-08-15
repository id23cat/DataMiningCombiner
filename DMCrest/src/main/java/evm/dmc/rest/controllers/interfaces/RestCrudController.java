package evm.dmc.rest.controllers.interfaces;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import evm.dmc.webApi.dto.AbstractDto;

public interface RestCrudController<T extends AbstractDto> 
	extends InstanceAdderController<T>, InstanceGetterController<T>, InstanceDeleterController<T>{
	
}
