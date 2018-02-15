package evm.dmc.web.service.impls;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
import evm.dmc.web.service.DataPreviewService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.ProjectService;

@Service
public class MetaDataServiceImpl implements MetaDataService{
	
	private MetaDataRepository metaDataRepository;
	
	private ProjectService projectService;
	
	private DataPreviewService previewService;
	
	
	@Autowired
	public MetaDataServiceImpl(MetaDataRepository metaDataRepository, ProjectService projectService,
			DataPreviewService previewService) {
		super();
		this.metaDataRepository = metaDataRepository;
		this.projectService = projectService;
		this.previewService = previewService;
	}


	@Override
	@Transactional
	public MetaData save(MetaData meta) {
		return metaDataRepository.save(meta);
	}
	
	
	/**
     * Use default delimiter MetaData.DEFAULT_DELIMITER
     * @param accountName
     * @param projectName
     * @param fileName
     * @param type
     * @param description
     * @return
     */
	@Override
    public  MetaData getMetaData(ProjectModel project, Path fullFilePath,
    		DataSrcDstType type, String description, String delimiter, boolean hasHeader) {
		
		// 1. Create DataStorageModel
    	DataStorageModel stroage = MetaDataService
    			.newDataStorageModel(fullFilePath, type, delimiter, hasHeader);
    	
    	// 2. Create MetaData, assign DataStorageModel
    	MetaData meta = MetaDataService.newMetaData(fullFilePath.getFileName().toString(), description, stroage);
    	
    	// 3. Persist new MetaData -- it is important to do before starting save process 
    	meta = persistMetadata(meta, project);
    	
    	return meta;
    }
    
	@Override
    @Transactional
    public MetaData persistMetadata(MetaData meta, ProjectModel project) {
    	meta.setProject(project);
//    	meta = dataRepository.save(meta);
    	return projectService.persistNewData(project, meta);
    }
    
	@Override
	@Transactional(propagation=Propagation.NESTED, isolation=Isolation.READ_COMMITTED)
    public DataPreview persistPreview(MetaData meta, List<String> previewLines) {
		DataPreview preview = getPreview(previewLines, 
				meta.getStorage().isHasHeader(), meta.getStorage().getDelimiter());
		
    	preview.setMetaDataId(meta.getId());
    	preview = previewService.save(preview);
    	meta.setPreviewId(preview.getId());
    	return preview;
    }
	
	@Override
	@Transactional(propagation=Propagation.NESTED, isolation=Isolation.READ_COMMITTED)
	public MetaData generateAndPersistAttrubutes(MetaData meta, DataPreview preview) {
		// Construct DataAttributes
    	List<DataAttribute> attributes = getDataAttributes(preview);
    	
    	// store meta data
    	return persistAttrubutes(meta, attributes);
	}
	
	@Override
	public MetaData persistAttrubutes(MetaData meta, List<DataAttribute> attributes) {
    	if(meta.getAttributes().size() == attributes.size()) {
    		setAttributesPreview(meta, attributes);
    	} else {
    		setAttributesCollection(meta, attributes);
    	}
    	
    	return save(meta);
    }
    
	@Override
	public List<DataAttribute> getDataAttributes(DataPreview preview) {
		List<DataAttribute> attributes = MetaDataService.streamLine(preview.getHeader(), preview.getDelimiter())
				.map((atrName) -> new DataAttribute(atrName)).collect(Collectors.toList());

		List<String> dataLines = new ArrayList<>(preview.getData());
		for (DataAttribute attribute : attributes) {
			for (int i = 0; i < dataLines.size(); i++) {
				String[] res = StringUtils.split(dataLines.get(i), preview.getDelimiter());
				attribute.addLine(res[0]);
				dataLines.set(i, res[1]);
			}
			attribute.setType(tryToPrdictType(attribute.getLines()));
		}

		return attributes;
	}
	
	private DataPreview getPreview(List<String> lines, boolean hasHeader, String delimiter) {
		String headerLine;
		if(hasHeader) {
			headerLine = lines.remove(0);
		} else { // generate indexes
			int size = MetaDataService.listLine(lines.get(0), delimiter).size();
			String currentDelimiter = String.valueOf(delimiter.charAt(0));
			StringJoiner joiner = new StringJoiner(currentDelimiter);
			
			for(int i=0; i<size; size++){
				joiner.add(String.valueOf(i));
			}
			headerLine = joiner.toString();
		}
		
		DataPreview data = DataPreviewService.newDataPreview(headerLine, lines, delimiter);
		return data;
	}
	
    /**
	 * Creates new content of Attributes map, using argument list
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
	 * @param list
	 */
	private void setAttributesPreview(MetaData meta, Collection<DataAttribute> list) {
		list.stream().parallel()
			.forEach(attr -> meta.getAttributes().get(attr.getName()).setLines(attr.getLines()));
	}
	
	public AttributeType tryToPrdictType(List<String> data) {
    	AttributeType type = AttributeType.STRING;
    	if(isNominal(data))
    		type = AttributeType.NOMINAL;
    	
    	List<AttributeType> types = new ArrayList<>(data.size());
    	for(int i=0; i<data.size(); i++) {
    		if(org.apache.commons.lang3.StringUtils.isNumeric(data.get(i))){
    			types.set(i, AttributeType.NUMERIC);
    		} else if(data.get(i).matches("[0-9:-]+")) {
    			types.set(i, AttributeType.DATE);
    		}
    	}
    	
    	List<AttributeType> total = types.stream().distinct().collect(Collectors.toList());
    	if(total.size() == 1) {
    		type = total.get(0);
    	}
    	
    	return type;
    }
    
    private boolean isNominal(List<String> data) {
    	Set<String> set = new HashSet<>(data);
    	return ! (set.size() == data.size());
    }

}
