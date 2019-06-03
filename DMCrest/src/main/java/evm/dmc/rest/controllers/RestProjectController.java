package evm.dmc.rest.controllers;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.model.service.AccountService;
import evm.dmc.model.service.ProjectService;
import evm.dmc.rest.annotations.HateoasRelation;
import evm.dmc.rest.annotations.HateoasRelationChildren;
import evm.dmc.webApi.dto.ProjectDto;
import evm.dmc.webApi.exceptions.AccountNotFoundException;
import evm.dmc.webApi.exceptions.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS REST API controller for Project model
 *
 * @see evm.dmc.api.model.ProjectModel
 */
@RestController
@RequestMapping(RestProjectController.BASE_URL)
@HateoasRelationChildren({RestDatasetController.class})
@CrossOrigin(origins = "*")
@Slf4j
public class RestProjectController extends AbstractRestCrudController<ProjectDto> {

    final static String BASE_URL = "/rest/{accountId}/project";

    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final AccountService accountService;

    @Autowired
    public RestProjectController(
            ModelMapper modelMapper,
            ProjectService projectService,
            AccountService accountService) {

        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.accountService = accountService;
    }

    @Override
    @PostMapping
    @HateoasRelation("addProject")
    public ProjectDto addInstance(ProjectDto dto, @PathVariable Long accountId, Long projectId) {

        Account account = accountService.get(accountId)
                .orElseThrow(AccountNotFoundException.supplier(accountId));

        ProjectModel projectModel = convertToEntity(dto);
        projectModel = projectService.addProject(account.getId(), projectModel);

        return convertToDto(projectModel);
    }

    @Override
    @PutMapping
    @HateoasRelation("updateProject")
    public ProjectDto updateInstance(ProjectDto dto, @PathVariable Long accountId, Long projectId) {

        ProjectModel model = convertToEntity(dto);
        projectService.save(model);

        return convertToDto(projectService.getById(dto.getDtoId())
                .orElseThrow(ProjectNotFoundException.supplier(dto.getDtoId())));
    }

    @Override
    @DeleteMapping("/{projectId}")
    @HateoasRelation("deleteProject")
    public ProjectDto deleteInstance(@PathVariable Long accountId, @PathVariable Long projectId, Long entityId) {

        Account account = accountService.get(accountId)
                .orElseThrow(AccountNotFoundException.supplier(accountId));

        ProjectDto dto = convertToDto(projectService.getById(projectId)
                .orElseThrow(ProjectNotFoundException.supplier(projectId)));

        projectService.deleteById(account, dto.getDtoId());
        return dto;
    }

    @Override
    @GetMapping("/{projectId}")
    @HateoasRelation("getProject")
    public ProjectDto getInstance(@PathVariable Long accountId, @PathVariable Long projectId, Long entityId) {

        return convertToDto(projectService.getById(projectId)
                .orElseThrow(ProjectNotFoundException.supplier(projectId)));
    }

    @Override
    @GetMapping("/all")
    @HateoasRelation("getProjectList")
    public List<ProjectDto> getInstanceList(@PathVariable Long accountId, Long projectId) {

        List<ProjectModel> modelList = projectService.getAllAsList();
        List<ProjectDto> dtoList = new ArrayList<>();
        for (ProjectModel model : modelList) {
            dtoList.add(convertToDto(model));
        }

        return dtoList;
    }

    private ProjectDto convertToDto(ProjectModel account) {

        return modelMapper.map(account, ProjectDto.class);
    }

    private ProjectModel convertToEntity(ProjectDto dto) {

        return modelMapper.map(dto, ProjectModel.class);
    }
}
