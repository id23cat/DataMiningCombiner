package evm.dmc.model.service.impls;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.model.repositories.DataPreviewRepository;
import evm.dmc.model.service.DataPreviewService;

@Service
public class DataPreviewServiceImpl implements DataPreviewService {
    @Autowired
    DataPreviewRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Optional<DataPreview> getByMetaDataId(Long metaDataId) {
        return repo.findByMetaDataId(metaDataId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataPreview> getForMetaData(MetaData mdata) {
        return repo.findByMetaDataId(mdata.getId());
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public DataPreview save(DataPreview preview) {
        // care about metaDataId uniqueness
        if (preview.getId() == null && repo.findByMetaDataId(preview.getMetaDataId()).isPresent())
            repo.deleteByMetaDataId(preview.getMetaDataId());
        return repo.save(preview);
    }

    @Override
    @Transactional
    public DataPreview delete(DataPreview preview) {
        repo.delete(preview);
        return null;
    }

    @Override
    @Transactional
//	public void deleteAllByMetaDataIds(Stream<Long> ids) {
    public void deleteAllByMetaDataIds(Set<Long> ids) {
        repo.deleteByMetaDataIdIn(ids);
    }

    @Override
    @Transactional
    public void deleteByMetaDataId(Long mid) {
        repo.deleteByMetaDataId(mid);
    }
}
