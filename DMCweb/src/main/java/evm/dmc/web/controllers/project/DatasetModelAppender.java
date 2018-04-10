package evm.dmc.web.controllers.project;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.controllers.CheckboxNamesBean;
import evm.dmc.web.service.DataSetProperties;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DatasetModelAppender {
	@Autowired
	private DataStorageService dataStorageService;
	
	@Autowired
	private MetaDataService metaDataService;
	public Model addAttributesToModel(Model model, ProjectModel project) {
//		Set<MetaData> sortedSet = new TreeSet<>(
//				(meta1, meta2) -> meta1.getName().compareTo(meta2.getName()));
//		Set<MetaData> dataSets = metaDataService.getForProject(project);
		List<MetaData> dataSets = metaDataService.getForProjectSortedBy(project, "name");
		
//		sortedSet.addAll(dataSets);
//		model.addAttribute(DatasetController.MODEL_DataSets, sortedSet);
		model.addAttribute(DatasetController.MODEL_DataSets, dataSets);
		
		DataSetProperties datasetProps = new DataSetProperties();
		model.addAttribute(DatasetController.MODEL_DataSetProps, datasetProps);
		
		CheckboxNamesBean backNamesBean = new CheckboxNamesBean();
		model.addAttribute(DatasetController.MODEL_SelectedNamesBean, backNamesBean);
		
		return setURLs(model, project);
	}
	
	public Model addAttributesToModel(Model model, ProjectModel project, Optional<MetaData> metaData) {
//		model = addAttributesToModel(model, project);
		model = setURLs(model, project);
		if(metaData.isPresent()) {
			log.debug("MetaData is found: {}", metaData.get().getName());
			model.addAttribute(DatasetController.MODEL_MetaData, metaData.get());
			model.addAttribute(DatasetController.MODEL_Preview, dataStorageService.getPreview(metaData.get()));
//			DataSetProperties hasHeader = (DataSetProperties) model.asMap().get(DatasetController.MODEL_DataSetProps);
//			hasHeader.setHasHeader(metaData.get().getStorage().isHasHeader());
			
			DataSetProperties datasetProps = new DataSetProperties();
			model.addAttribute(DatasetController.MODEL_DataSetProps, datasetProps);
			datasetProps.setHasHeader(dataStorageService.getDataStorage(metaData.get()).isHasHeader());
//			model.addAttribute(MODEL_HeaderItems, new DataPreview.ItemsList(preview.get().getHeaderItems()));
		} else {
			model = addAttributesToModel(model, project);
		}
		return model;
	}
	
	private Model setURLs(Model model, ProjectModel project) {
		// setting BASE_URL
		UriComponents baseUri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL)
				.buildAndExpand(project.getName());
		model.addAttribute(DatasetController.MODEL_DataBaseURL, baseUri.toString());
		
		// setting URL for uploading new dataset
//		UriComponents srcUploadUri = UriComponentsBuilder.fromPath(DatasetController.URL_SetSource)
//				.buildAndExpand(project.getName());
//		model.addAttribute(DatasetController.MODEL_DataUploadURL, srcUploadUri.toUriString());
		
		// setting URL for uploading changes to dataset attributes
		UriComponents srcAttrdUri = UriComponentsBuilder.fromPath(DatasetController.URL_SetAttributes)
				.buildAndExpand(project.getName());
		model.addAttribute(DatasetController.MODEL_DataAttributesURL, srcAttrdUri.toUriString());
		
		return model;
	}
}
