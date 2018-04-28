package evm.dmc.model.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.datapreview.DataPreview;

public interface DataPreviewRepository extends JpaRepository<DataPreview, Long>{
	Optional<DataPreview> findByMetaDataId(Long metaDataId);
	
	@Query("SELECT DISTINCT preview.metaDataId FROM DataPreview preview")
	Set<Long> findDistinctMetaDataId();

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	void deleteByMetaDataId(Long metaDataId);
	
//	void deleteByMetaDataIdIn(Stream<Long> metaDataIds);
	void deleteByMetaDataIdIn(Set<Long> metaDataIds);
}
