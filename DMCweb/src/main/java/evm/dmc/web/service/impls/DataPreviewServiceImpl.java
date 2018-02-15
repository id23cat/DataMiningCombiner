package evm.dmc.web.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.model.repositories.DataPreviewRepository;
import evm.dmc.web.service.DataPreviewService;

@Service
public class DataPreviewServiceImpl implements DataPreviewService {
	@Autowired
	DataPreviewRepository repo;

	@Override
	@Transactional(readOnly=true)
	public DataPreview getByMetaDataId(Long metaDataId) {
		return repo.findByMetaDataId(metaDataId);
	}

	@Override
	@Transactional(readOnly=true)
	public DataPreview getForMetaData(MetaData mdata) {
		return repo.findByMetaDataId(mdata.getId());
	}

	@Override
	@Transactional
	public DataPreview save(DataPreview preview) {
		
		return repo.save(preview);
	}

}
