package evm.dmc.rest.controllers.interfaces;

import evm.dmc.webApi.dto.AbstractDto;

/**
 * defines HATEOAS REST API interface for getting instances
 *
 * @param <T> - DTO
 */
public interface InstanceAdderController<T extends AbstractDto> {

    /**
     * adds new instance
     *
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @param dto       DTO for adding
     * @return added DTO
     */
    T addInstance(T dto, Long accountId, Long projectId);

    /**
     * updates existing instance
     *
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @param dto       DTO with updated information
     * @return updated DTO
     */
    T updateInstance(T dto, Long accountId, Long projectId);
}
