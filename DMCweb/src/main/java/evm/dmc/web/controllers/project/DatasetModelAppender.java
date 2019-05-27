package evm.dmc.web.controllers.project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        List<MetaData> dataSets = metaDataService.getForProjectSortedBy(project, "name");

        model.addAttribute(DatasetController.MODEL_DataSets, dataSets);

        DataSetProperties datasetProps = new DataSetProperties("", "", true);
        model.addAttribute(DatasetController.MODEL_DataSetProps, datasetProps);

        CheckboxNamesBean backNamesBean = new CheckboxNamesBean();
        model.addAttribute(DatasetController.MODEL_SelectedNamesBean, backNamesBean);

        return setURLs(model, project);
    }

    public Model addAttributesToModel(Model model, ProjectModel project, Optional<MetaData> metaData) {
        setURLs(model, project);
        if (metaData.isPresent()) {
            log.debug("-== MetaData is found: {}", metaData.get());
            log.debug("-== Attributes: {}", metaData.get().getAttributes());
            model.addAttribute(DatasetController.MODEL_MetaData, metaData.get());
            model.addAttribute(DatasetController.MODEL_Preview, dataStorageService.getPreview(metaData.get()));

            DataSetProperties datasetProps = DataSetProperties
                    .builder()
                    .hasHeader(dataStorageService.getDataStorage(metaData.get()).isHasHeader())
                    .build();

            model.addAttribute(DatasetController.MODEL_DataSetProps, datasetProps);
        } else {
            model = addAttributesToModel(model, project);
        }
        return model;
    }

    private Model setURLs(Model model, ProjectModel project) {
        UriComponents baseUri = UriComponentsBuilder.fromPath(DatasetController.BASE_URL)
                .buildAndExpand(project.getName());
        model.addAttribute(DatasetController.MODEL_DataBaseURL, baseUri.toString());

        UriComponents srcAttrdUri = UriComponentsBuilder.fromPath(DatasetController.URL_SetAttributes)
                .buildAndExpand(project.getName());
        model.addAttribute(DatasetController.MODEL_DataAttributesURL, srcAttrdUri.toUriString());

        return model;
    }
}
