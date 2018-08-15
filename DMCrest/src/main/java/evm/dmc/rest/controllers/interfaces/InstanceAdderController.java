package evm.dmc.rest.controllers.interfaces;

import evm.dmc.webApi.dto.AbstractDto;

public interface InstanceAdderController<T extends AbstractDto> {
	T postNewInstance(Long accountId, T dto);

	T psotUpdateInstance(Long accountId, T dto);
}
