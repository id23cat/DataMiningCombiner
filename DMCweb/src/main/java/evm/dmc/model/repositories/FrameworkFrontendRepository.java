package evm.dmc.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FrameworkType;

public interface FrameworkFrontendRepository extends JpaRepository<FrameworkModel, Long>{
	Optional<FrameworkModel> findByType(FrameworkType type);
	Optional<FrameworkModel> findByName(String name);
	Optional<FrameworkModel> findByActive(boolean active);
}
