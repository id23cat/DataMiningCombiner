package evm.dmc.rest.controllers;

import evm.dmc.rest.controllers.interfaces.RestCrudController;
import evm.dmc.webApi.dto.AbstractDto;

/**
 * defines HATEOAS REST API common parent for CRUD operations with instances
 * @param <T> - DTO
 */
public abstract class AbstractRestCrudController<T extends AbstractDto>
	implements RestCrudController<T> {
}
