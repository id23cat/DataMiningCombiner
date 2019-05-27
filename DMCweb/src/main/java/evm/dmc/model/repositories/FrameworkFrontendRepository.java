package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import evm.dmc.api.model.FrameworkModel;
import evm.dmc.api.model.FrameworkType;

public interface FrameworkFrontendRepository extends JpaRepository<FrameworkModel, Long> {
    String FIND_ALL = "SELECT p FROM FrameworkModel p ORDER BY name ";

    List<FrameworkModel> findAll(Sort sort);

    @Query(FIND_ALL)
    Stream<FrameworkModel> streamAll();

    Optional<FrameworkModel> findByType(FrameworkType type);

    Optional<FrameworkModel> findByName(String name);

    Optional<FrameworkModel> findByActive(boolean active);
}
