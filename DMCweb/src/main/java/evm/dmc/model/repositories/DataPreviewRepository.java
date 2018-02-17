package evm.dmc.model.repositories;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import evm.dmc.api.model.datapreview.DataPreview;

public interface DataPreviewRepository extends JpaRepository<DataPreview, Long>{
	DataPreview findByMetaDataId(Long metaDataId);
	
	@Query("SELECT DISTINCT preview.metaDataId FROM DataPreview preview")
	Set<Long> findDistinctMetaDataId();

}
