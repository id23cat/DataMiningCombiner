package evm.dmc.rest.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.repositories.ProjectModelRepository;
import evm.dmc.model.service.AccountService;
import evm.dmc.model.service.ProjectService;
import evm.dmc.webApi.dto.ProjectDto;
import evm.dmc.webApi.exceptions.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(RestProjectControllerOld.BASE_URL)	// /rest/{accountId}/project
@Slf4j
public class RestProjectControllerOld {
	public final static String BASE_URL = "/rest/{accountId}/project";
	
	public final static String LINK_REL_projectsList = "projectsList";
	public final static String REQPARAM_projId = "id";
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ProjectModelRepository projectModelRepository;
	
	public static ResourceSupport projectsListLink(ResourceSupport resSupport, Long accountId) {
		Link listLink = linkTo(methodOn(RestProjectControllerOld.class)
				.getProjectsList(accountId))
				.withRel(LINK_REL_projectsList);
		resSupport.add(listLink);
		return resSupport;
	}
	
	public static ProjectDto selfLink(ProjectDto prjDto, Long accountId) {
		prjDto.add(linkTo(methodOn(RestProjectControllerOld.class)
				.getProject(accountId, prjDto.getProjectId()))
				.withSelfRel());
		return prjDto;
	}
	
	@GetMapping("/getall")
	@Transactional(readOnly=true)
	public List<ProjectDto> getProjectsList(@PathVariable final Long accountId) {
//		validateAccount(accountId);
		return projectModelRepository.findAllByAccountId(accountId)
				.map(this :: convertToDto)
				.peek((projDto) -> addLinks(projDto, accountId))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{projectId}")
	@Transactional(readOnly=true)
	public ProjectDto getProject(
			@PathVariable final Long accountId,
			@PathVariable Long projectId) {
		ProjectModel project = projectModelRepository.findByIdAndAccountId(projectId, accountId)
				.orElseThrow(() -> new ProjectNotFoundException("User " + accountId +
						" haven't project " + projectId));
		ProjectDto prjDto = convertToDto(project);
		return addLinks(prjDto, accountId);
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> add(
			@PathVariable Long accountId,
			@RequestBody ProjectDto projectDto) {
		ProjectModel project = convertToEntity(projectDto);
		
//		project = projectService.save(project);
		project = projectService.addProject(accountId, project);
		projectDto = convertToDto(project);
		
		return ResponseEntity.created(
				URI.create(
						selfLink(projectDto, accountId).getLink("self").getHref()))
//				.build();
				.body(projectDto);
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> delete(
			@PathVariable Long accountId,
			@RequestParam(REQPARAM_projId) Long id) {
		projectService.deleteById(accountService.get(accountId), id);
		return null;
		
	}
	
	private ProjectDto addLinks(ProjectDto prjDto, Long accountId) {
		selfLink(prjDto, accountId);
		projectsListLink(prjDto, accountId);
		RestDatasetController.datasetsListLink(prjDto, accountId, prjDto.getProjectId());
		return prjDto;
	}
	
	
	private ProjectDto convertToDto(ProjectModel project) {
		return modelMapper.map(project, ProjectDto.class);
		
	}
	
	private ProjectModel convertToEntity(ProjectDto dto) {
		return modelMapper.map(dto, ProjectModel.class);
	}
	
//	private void validateAccount(Long accountId) {
//		this.accountRepository
//			.findById(accountId)
//			.orElseThrow(AccountNotFoundException.supplier(accountId));
//	}
}
