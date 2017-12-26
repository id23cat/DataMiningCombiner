package evm.dmc.core.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.FunctionModel;

public interface FunctionModelCoreRepository extends JpaRepository<FunctionModel, Long> {

}
