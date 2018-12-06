package evm.dmc.rest.controllers.interfaces;

import java.util.List;

import evm.dmc.webApi.dto.AbstractDto;

/**
 * defines HATEOAS REST API interface for getting instances
 * @param <T> - DTO
 */
public interface InstanceGetterController<T extends AbstractDto> {

	/**
	 * finds existing instance
	 * @param accountId Account model identifier
	 * @param projectId Project model identifier
	 * @param entityId Entity identifier
	 * @return found DTO
	 */
	T getInstance(Long accountId, Long projectId, Long entityId);

	/**
	 * finds all existing instances
	 * @param accountId Account model identifier
	 * @param projectId Project model identifier
	 * @return found list of DTO
	 */
	List<T> getInstanceList(Long accountId, Long projectId);
}
