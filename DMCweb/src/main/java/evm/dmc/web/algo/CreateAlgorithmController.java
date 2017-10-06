package evm.dmc.web.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.api.model.AlgorithmModel;
import evm.dmc.config.ViewsConfig;
import evm.dmc.core.api.Project;
import evm.dmc.web.ShowTableController;
import evm.dmc.web.config.annotations.DefaultProject;

@Controller
@RequestMapping("/{userId}/createalg")
//@RequestMapping("/{userId}/createalg")
public class CreateAlgorithmController {
	private static final Logger logger = LoggerFactory.getLogger(CreateAlgorithmController.class);
	Project project;

	/*@Value("${views.createalg}")
	String createAlgView = "createalg";*/
	
	public CreateAlgorithmController(@Autowired @DefaultProject Project project) {
		logger.debug("Autowired Project: " + project.getAlgorithm().toString());
		this.project = project;		
	}
	
	public void setProject(Project project){
		this.project = project;
	}
	
	@GetMapping
	String createAlgorithm(@PathVariable String userId, Model model) {
		logger.debug("Inside CreateAlgorithmController: " + userId);
		
		AlgorithmModel algModel = project.createAlgorithm().getModel();
		model.addAttribute("algModel", algModel);
//		model.addAttribute("view", createAlgView);
		return "createalg";
	}

}
