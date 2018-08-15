package evm.dmc.rest.controllers.interfaces;

import evm.dmc.webApi.dto.AbstractDto;

public interface InstanceDeleterController <T extends AbstractDto> {
	T deleteInstance(Long accountId, Long id);
	T deleteInstance(Long accountId, T dto);
}
