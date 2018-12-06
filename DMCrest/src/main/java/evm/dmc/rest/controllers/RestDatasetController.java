package evm.dmc.rest.controllers;

import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.model.service.MetaDataService;
import evm.dmc.rest.annotations.HateoasRelation;
import evm.dmc.webApi.dto.MetaDataDto;
import evm.dmc.webApi.exceptions.MetaDataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(RestDatasetController.BASE_URL)
@Slf4j
public class RestDatasetController extends AbstractRestCrudController<MetaDataDto> {

    final static String BASE_URL = "/rest/{accountId}/project/{projectId}/dataset";

    private MetaDataService metaDataService;
    private MetaDataRepository metaDataRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RestDatasetController(
            MetaDataService metaDataService,
            MetaDataRepository metaDataRepository,
            ModelMapper modelMapper) {

        this.metaDataService = metaDataService;
        this.metaDataRepository = metaDataRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @PostMapping
    @HateoasRelation("addDataset")
    public MetaDataDto addInstance(MetaDataDto dto, @PathVariable Long accountId, @PathVariable Long projectId) {
        return null;
    }

    @Override
    @PutMapping
    @HateoasRelation("updateDataset")
    public MetaDataDto updateInstance(MetaDataDto dto, @PathVariable Long accountId, @PathVariable Long projectId) {
        return null;
    }

    @Override
    @DeleteMapping("/{entityId}")
    @HateoasRelation("deleteProject")
    public MetaDataDto deleteInstance(@PathVariable Long accountId, @PathVariable Long projectId, @PathVariable Long entityId) {
        return null;
    }

    @Override
    @GetMapping("/{entityId}")
    @HateoasRelation("getDataset")
    public MetaDataDto getInstance(@PathVariable Long accountId, @PathVariable Long projectId, @PathVariable Long entityId) {

        return convertToDto(metaDataRepository.findByIdAndProjectId(entityId, projectId)
                .orElseThrow(MetaDataNotFoundException.supplier(entityId)));
    }

    @Override
    @GetMapping("/all")
    @HateoasRelation("getDatasetList")
    public List<MetaDataDto> getInstanceList(@PathVariable Long accountId, @PathVariable Long projectId) {

        List<MetaData> modelList = metaDataService.getProjectDatasetsAsList(projectId);
        List<MetaDataDto> dtoList = new ArrayList<>();
        for (MetaData dataset : modelList) {
            dtoList.add(convertToDto(dataset));
        }

        return dtoList;
    }

    private MetaDataDto convertToDto(MetaData account) {

        return modelMapper.map(account, MetaDataDto.class);
    }

    private MetaData convertToEntity(MetaDataDto dto) {

        return modelMapper.map(dto, MetaData.class);
    }
}
