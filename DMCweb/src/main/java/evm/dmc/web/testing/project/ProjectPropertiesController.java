package evm.dmc.web.testing.project;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.Project;
import evm.dmc.web.config.annotations.DefaultProject;

@Controller
@RequestMapping("/testing/{userId}/project/")
//@SessionAttributes("{projectsList, currentProject}")
public class ProjectPropertiesController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectPropertiesController.class);
	
	ProjectModel project;
	
//	@ModelAttribute("projectsList")
//	public List<Project> setUpProjectsList() {
//		logger.debug("Inside setUpProjectsList()");
//		return new ArrayList<Project>();
//	}
	
	/**
	 * Adds new project to ProjectsList if "currentProject" session variable is empty
	 * @param project 
	 * @param projectList
	 * @return 
	 */
//	@ModelAttribute("currentProject")
//	public Project setUpCurrentProject(@Autowired @DefaultProject Project project, 
//			@ModelAttribute("projectsList") List<Project> projectList) {
//		logger.debug("Inside setUpCurrentProject()");
//		projectList.add(project);
//		return project;
//	}
	
	
	@GetMapping("newproject")
	String newProject(Model model) {
		project = new ProjectModel();
		model.addAttribute("project", project);
		return "testing/newproject";
	}
	
	@GetMapping("{projId}/main")
	String getProjectMain(Model model) {
		logger.debug("Calling main project");
		return "testing/project-main";
	}

}
