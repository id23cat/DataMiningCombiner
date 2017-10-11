package evm.dmc.model.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import evm.dmc.api.model.ProjectModel;

@Repository
public class ProjectRepositoryListImpl implements ProjectRepository{
	List<ProjectModel> projList = new ArrayList<>();
	
	@Override
	public void addNew(ProjectModel proModel){
		projList.add(proModel);
		
	}
	
	@Override
	public ProjectModel getFirst() {
		return projList.stream().findFirst().get();
	}
}
