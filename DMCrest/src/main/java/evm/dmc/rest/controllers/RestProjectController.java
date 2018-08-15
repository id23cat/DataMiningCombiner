package evm.dmc.rest.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.repositories.ProjectModelRepository;
import evm.dmc.model.service.AccountService;
import evm.dmc.model.service.ProjectService;
import evm.dmc.webApi.dto.ProjectDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(RestProjectController.BASE_URL)	// /rest/{accountId}/project
@Slf4j
public class RestProjectController extends AbstractRestCrudController<ProjectDto> {
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

	@Override
	public ProjectDto getInstance(Long accountId, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProjectDto> getInstancesList(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectDto postNewInstance(Long accountId, ProjectDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectDto psotUpdateInstance(Long accountId, ProjectDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectDto deleteInstance(Long accountId, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectDto deleteInstance(Long accountId, ProjectDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListRelatioinName() {
		// TODO Auto-generated method stub
		return null;
	}

}
