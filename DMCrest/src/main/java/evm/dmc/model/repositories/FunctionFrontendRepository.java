package evm.dmc.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import evm.dmc.api.model.FunctionModel;
import evm.dmc.api.model.FunctionType;


public interface FunctionFrontendRepository extends JpaRepository<FunctionModel, Long> {
    String FIND_DIFF_TYPES = "SELECT DISTINCT f.type FROM FunctionModel f";
    String FIND_ALL_FUNCTIONS = "SELECT f FROM FunctionModel f";
    String FIND_BY_WORD = "SELECT f FROM FunctionModel f WHERE LOWER(f.name) LIKE LOWER('%:name%')";

    <T extends FunctionModel> Stream<T> findByType(FunctionType type);

    Optional<FunctionModel> findById(Long id);

    Optional<FunctionModel> findByName(String name);

    Optional<String> getDescriptionByName(String name);

    //	@Query(FIND_BY_WORD)
//	Optional<FunctionModel> findByWord(@Param("name") String word);
    Stream<FunctionModel> findByNameIgnoreCaseContaining(String name);

    /**
     * See: {@link import org.springframework.data.jpa.repository.QueryHints;}
     */
//	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query(FIND_ALL_FUNCTIONS)
    Stream<FunctionModel> StreamAllFunctions();

    @Async
    CompletableFuture<List<FunctionModel>> readAllBy();

    @Query(FIND_DIFF_TYPES)
    Stream<FunctionType> findDistinctTypes();

}
