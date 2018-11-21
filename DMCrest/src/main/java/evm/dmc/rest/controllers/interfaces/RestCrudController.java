package evm.dmc.rest.controllers.interfaces;

import evm.dmc.webApi.dto.AbstractDto;

/**
 * defines HATEOAS REST API interface for CRUD operations with instances
 * @param <T> - DTO
 */
public interface RestCrudController<T extends AbstractDto> extends
		InstanceGetterController<T>,
		InstanceAdderController<T>,
		InstanceDeleterController<T>{
}
