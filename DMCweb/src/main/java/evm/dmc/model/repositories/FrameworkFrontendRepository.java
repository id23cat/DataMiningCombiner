package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FrameworkType;

public interface FrameworkFrontendRepository extends JpaRepository<FrameworkModel, Long>{
	List<FrameworkModel> findAll(Sort sort);
	Optional<FrameworkModel> findByType(FrameworkType type);
	Optional<FrameworkModel> findByName(String name);
	Optional<FrameworkModel> findByActive(boolean active);
}
