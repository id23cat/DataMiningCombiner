package evm.dmc.model.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import evm.dmc.api.model.datapreview.DataPreview;

public interface DataPreviewRepository extends JpaRepository<DataPreview, Long>{
	public interface MetaDataId {
		Long getMetaDataId();
	}
	
	DataPreview findByMetaDataId(Long metaDataId);
	
	Collection<MetaDataId> findAllDistinct();

}
