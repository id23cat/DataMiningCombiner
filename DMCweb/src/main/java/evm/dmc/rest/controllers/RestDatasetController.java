package evm.dmc.rest.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evm.dmc.api.model.data.MetaData;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.rest.dto.MetaDataDto;
import evm.dmc.rest.dto.ProjectDto;
import evm.dmc.web.exceptions.MetaDataNotFoundException;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(RestDatasetController.BASE_URL)	// /rest/{accountId}/project/{projectId}/dataset
@Slf4j
public class RestDatasetController {
	public final static String BASE_URL = "/rest/{accountId}/project/{projectId}/dataset";
	
	public final static String LINK_REL_datasetsList = "datasetsList";
	
	@Autowired
	private MetaDataService metaDataService;
	
	@Autowired
	private MetaDataRepository metadataRepository;
	
	@Autowired
	private DataStorageService dataStorageService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public static ResourceSupport datasetsListLink(ResourceSupport resSupport, Long accountId, Long projectId) {
		Link listLink = linkTo(methodOn(RestDatasetController.class).getDatasetsList(accountId, projectId))
				.withRel(LINK_REL_datasetsList);
		resSupport.add(listLink);
		return resSupport;
	}
	
	public static MetaDataDto selfLink(MetaDataDto metaDto, Long accountId, Long projectId) {
		Link selfLink = linkTo(methodOn(RestDatasetController.class)
				.getDataSet(accountId, projectId, metaDto.getMetaDataId()))
				.withSelfRel();
		metaDto.add(selfLink);
		return metaDto;
				
	}
	
	@GetMapping
	@Transactional(readOnly=true)
	public List<MetaDataDto> getDatasetsList(
			@PathVariable Long accountId,
			@PathVariable Long projectId) {
		return metadataRepository.findAllByProjectId(projectId)
				.filter(matchBelonging(accountId))
				.map(this :: convertToDto)
				.peek((metaDto) -> addLinks(metaDto, accountId, projectId))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{datasetId}")
	@Transactional(readOnly=true)
	public MetaDataDto getDataSet(
			@PathVariable Long accountId,
			@PathVariable Long projectId, 
			@PathVariable Long datasetId) {
		MetaData metaData = metadataRepository.findByIdAndProjectId(datasetId, projectId)
//				.filter(matchBelonging(accountId))
				.orElseThrow(()-> new MetaDataNotFoundException("MetaData with id=" + datasetId 
						+ " belongs to project=" + projectId 
						+ " and account=" + accountId
						+ " has not found"));
		return addLinks(convertToDto(metaData), accountId, projectId);
	}
	
	private MetaDataDto addLinks(MetaDataDto metaDto, Long accountId, Long projectId) {
		selfLink(metaDto, accountId, projectId);
		datasetsListLink(metaDto, accountId, projectId);
		return metaDto;
	}
	
	private MetaDataDto convertToDto(MetaData meta) {
		return modelMapper.map(meta, MetaDataDto.class);
	}
	
	@Transactional
	private Predicate<? super MetaData> matchBelonging(final Long accountId) {
//		return (meta) -> meta.getProject().getAccount().getId() == accountId;
		
		return (meta) -> {
			log.debug("-== Account: {}", meta.getProject().getAccount());
			return meta.getProject().getAccount().getId() == accountId;
		};
	}

}
