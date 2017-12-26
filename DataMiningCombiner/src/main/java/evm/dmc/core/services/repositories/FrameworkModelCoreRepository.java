package evm.dmc.core.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.FrameworkModel;

public interface FrameworkModelCoreRepository extends JpaRepository<FrameworkModel, Long> {

}
