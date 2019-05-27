package evm.dmc.model.service.impls;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.core.api.AttributeType;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.model.service.DataPreviewService;
import evm.dmc.model.service.DataSetProperties;
import evm.dmc.model.service.MetaDataService;
import evm.dmc.webApi.exceptions.MetaDataNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetaDataServiceImpl implements MetaDataService {

    private MetaDataRepository metaDataRepository;

    private DataPreviewService previewService;

    @Autowired
    EntityManager em;

    @Autowired
    public MetaDataServiceImpl(MetaDataRepository metaDataRepository, DataPreviewService previewService) {
        super();
        this.metaDataRepository = metaDataRepository;
        this.previewService = previewService;
    }


    @Override
    @Transactional
    public MetaData save(MetaData metaData) {
        return metaDataRepository.save(metaData);
    }

    @Override
    @Transactional
    public void delete(ProjectModel project, Set<String> names) {
        Set<MetaData> metaSet = metaDataRepository
                .findByProjectAndNameIn(project, names)
                .collect(Collectors.toSet());
        Set<Long> idsSet = metaSet.parallelStream()
                .map(MetaData::getId)
                .collect(Collectors.toSet());
        previewService.deleteAllByMetaDataIds(idsSet);
        metaDataRepository.deleteAll(metaSet);
    }


    @Override
    @Transactional
    public void delete(MetaData metaData) {
        metaDataRepository.delete(metaData);
    }


    /**
     * Use default delimiter MetaData.DEFAULT_DELIMITER
     */
    @Override
    public MetaData getMetaData(ProjectModel project, Path fullFilePath,
                                Path baseDir, DataSrcDstType type, String delimiter, DataSetProperties datasetProperities) {
        log.debug("==Creating new DataStorageModel: {}", fullFilePath.toUri().toString() + " with " + baseDir.toUri().toString());
        // 1. Create DataStorageModel
        DataStorageModel stroage = MetaDataService
                .newDataStorageModel(baseDir, fullFilePath, type, delimiter, datasetProperities.isHasHeader());

        // 2. Create MetaData, assign DataStorageModel
        MetaData meta = MetaDataService.newMetaData(datasetProperities.getName(), datasetProperities.getDescription(), stroage);

        // 3. Persist new MetaData -- it is important to do before starting save process
        meta = persistMetadata(meta, project);

        return meta;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<MetaData> getForProject(ProjectModel project) {
        return metaDataRepository.findByProject(project).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public List<MetaData> getForProjectSortedBy(ProjectModel project, String fieldName) {
        return metaDataRepository.findByProject(project, Sort.by(fieldName));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MetaData> getByProjectAndName(ProjectModel project, String name) {
        return metaDataRepository.findByProjectAndName(project, name);
//		return Optional.of(metaDataRepository.findByProjectAndName(project, name));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<MetaData> getByProjectAndName(ProjectModel project, Set<String> names) {
        return metaDataRepository.findByProjectAndNameIn(project, names).collect(Collectors.toSet());
    }

    @Override
    public Optional<DataPreview> getPreview(MetaData meta) {
        return previewService.getForMetaData(meta);
    }

    @Override
    @Transactional
    public MetaData persistMetadata(MetaData meta, ProjectModel project) {
        meta.setProject(project);
//    	meta = dataRepository.save(meta);
//    	return projectService.persistNewData(project, meta);
        return metaDataRepository.save(meta);
    }

    @Override
//	@Transactional
    public DataPreview createPreview(MetaData meta, List<String> previewLines) {
//		meta = em.merge(meta);
        DataPreview preview = getPreview(previewLines,
                meta.getStorage().isHasHeader(), meta.getStorage().getDelimiter());

        preview.setMetaDataId(meta.getId());
        return preview;
    }

    @Override
    @Transactional
    public Map<MetaData, DataPreview> persistPreview(MetaData meta, DataPreview preview) {
        Map<MetaData, DataPreview> map = new HashMap<>();
        map.put(meta, preview);

        return persistPreview(map);
    }

    @Override
    @Transactional
    public Map<MetaData, DataPreview> persistPreview(Map<MetaData, DataPreview> map) {
        MetaData meta = map.keySet().iterator().next();
        DataPreview preview = map.get(meta);
        map.remove(meta);

        meta = merge(meta);
        preview.setMetaDataId(meta.getId());
        preview = previewService.save(preview);

        meta.setPreviewId(preview.getId());
        meta = metaDataRepository.save(meta);

        map.put(meta, preview);
        return map;
    }

    @Override
//	@Transactional(propagation=Propagation.NESTED, isolation=Isolation.READ_COMMITTED)
//	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.READ_COMMITTED)
    @Transactional
    public MetaData generateAndPersistAttributes(MetaData meta, DataPreview preview) {
        // Construct DataAttributes
        List<DataAttribute> attributes = getDataAttributes(preview);

        log.debug("Persisting attributes: {}", attributes);
        log.debug(" to metaData: {}", meta);
        // store meta data
        return persistAttributes(meta, attributes);
    }

    @Override
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
    @Transactional
    public MetaData persistAttributes(MetaData meta, List<DataAttribute> attributes) {
        meta = merge(meta);
        if (meta.getAttributes().size() == attributes.size()) {
            setAttributesPreview(meta, attributes);
        } else {
            setAttributesCollection(meta, attributes);
        }
        log.debug("Persisting attributes in: {}", meta);
//    	em.
        meta = save(meta);
        return meta;
    }

    @Override
    public List<DataAttribute> getDataAttributes(DataPreview preview) {
        List<DataAttribute> attributes = MetaDataService.streamLine(preview.getHeader(), preview.getDelimiter())
                .map((atrName) -> DataAttribute.builder().name(atrName).build()).collect(Collectors.toList());

        List<String> dataLines = new ArrayList<>(preview.getData());
        for (DataAttribute attribute : attributes) {
            for (int i = 0; i < dataLines.size(); i++) {
                String[] res = Optional.ofNullable(
                        StringUtils.split(dataLines.get(i), preview.getDelimiter()))
                        .orElse(new String[]{dataLines.get(i), ""});

                attribute.addLine(res[0].trim());
                dataLines.set(i, res[1]);
            }
            attribute.setType(tryToPrdictType(attribute.getLines()));
        }

        return attributes;
    }

    @Override
    @Transactional
    public MetaData updateAttributes(ProjectModel project, MetaData metaAttribs) {
        Optional<MetaData> optMeta = getByProjectAndName(project, metaAttribs.getName());
        MetaData meta = optMeta.orElseThrow(
                () -> {
                    return new MetaDataNotFoundException("MetaData with name" +
                            metaAttribs.getName() +
                            " not found");
                });
        log.debug("-== metaAttribs: {}", metaAttribs);
        meta.getAttributes().replaceAll((k, v) -> {
            DataAttribute dattr = metaAttribs.getAttributes().get(k);
            log.debug("-== Key: {}", k);
            log.debug("-== Set: {}", dattr);
            log.debug("-== To: {}", v);
            if (dattr.getChecked() != null)
                v.setChecked(dattr.getChecked());
            if (dattr.getMultiplier() != null)
                v.setMultiplier(dattr.getMultiplier());
            if (dattr.getType() != null)
                v.setType(dattr.getType());
            return v;
        });
        return meta;
    }

    @Override
    @Transactional
    public List<MetaData> getProjectDatasetsAsList(Long projectId) {
        return getAsCollection(metaDataRepository.findAllByProjectId(projectId), Collectors.toList());
    }

    //	@Override
    private DataPreview getPreview(List<String> lines, boolean hasHeader, String delimiter) {
        String headerLine;
        delimiter = MetaDataService.getActiveDelimiters(lines.get(0), delimiter);
        if (hasHeader) {
            headerLine = lines.remove(0);
        } else { // generate indexes
            int size = MetaDataService.listLine(lines.get(0), delimiter).size();
            String currentDelimiter = String.valueOf(delimiter.charAt(0));
            StringJoiner joiner = new StringJoiner(currentDelimiter);

            for (int i = 0; i < size; i++) {
                joiner.add(String.valueOf(i));
            }
            headerLine = joiner.toString();
        }

        DataPreview data = DataPreviewService.newDataPreview(headerLine, lines, delimiter);
        return data;
    }

    /**
     * Creates new content of Attributes map, using argument list
     *
     * @param list
     */
    private void setAttributesCollection(MetaData meta, Collection<DataAttribute> list) {
        list.stream().parallel()
                .forEach(attr ->
                {
                    Optional.ofNullable(meta.getAttributes().putIfAbsent(attr.getName(), attr))
                            .ifPresent((val) -> meta.getAttributes().putIfAbsent(val + MetaData.DUPLICATION_POSTFIX, attr));
                });
    }

    /**
     * Copies preview values to existing attributes
     * Names of attributes must match
     *
     * @param list
     */
    private void setAttributesPreview(MetaData meta, Collection<DataAttribute> list) {
        list.stream().parallel()
                .forEach(attr -> meta.getAttributes().get(attr.getName()).setLines(attr.getLines()));
    }

    public AttributeType tryToPrdictType(List<String> data) {
        AttributeType type = AttributeType.STRING;
        if (isNominal(data))
            type = AttributeType.NOMINAL;

        List<AttributeType> types = new LinkedList<>();
        for (int i = 0; i < data.size(); i++) {
            String value = data.get(i);
            if (isNumeric(value)) {
                types.add(AttributeType.NUMERIC);
            } else if (isDate(value)) {
                types.add(AttributeType.DATE);
            }
        }

        List<AttributeType> total = types.stream().distinct().collect(Collectors.toList());
        if (total.size() == 1) {
            type = total.get(0);
        }

        return type;
    }

    private boolean isNominal(List<String> data) {
        Set<String> set = new HashSet<>(data);
        return !(set.size() == data.size());
    }

    private boolean isNumeric(String data) {
        if (data.contains(":") || data.contains("-"))
            return false;
        return data.matches("[+-]?[\\d.+]+");
    }

    private boolean isDate(String data) {
        return data.matches("[0-9:-]+");
    }

    private MetaData merge(MetaData meta) {
        return em.merge(meta);
    }

    private static <T extends Collection<MetaData>> T getAsCollection(
            Stream<MetaData> stream, Collector<MetaData, ?, T> collector) {

        T dataSetCollection = stream.collect(collector);
        stream.close();
        return dataSetCollection;
    }

}
