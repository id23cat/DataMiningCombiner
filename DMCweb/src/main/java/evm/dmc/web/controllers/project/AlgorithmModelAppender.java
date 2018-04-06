package evm.dmc.web.controllers.project;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.web.service.AlgorithmService;

@Component
public class AlgorithmModelAppender {
	@Autowired
	private AlgorithmService algorithmService;
	
	public Model addAttributesToModel(Model model, ProjectModel project) {
		Set<Algorithm> algSet = algorithmService.getForProject(project);
		
		// algorithm attributes
		model.addAttribute(AlgorithmController.MODEL_AlgorithmsSet, algSet);
		UriComponents baseUrl = UriComponentsBuilder.fromPath(AlgorithmController.BASE_URL)
				.buildAndExpand(project.getName());
		model.addAttribute(AlgorithmController.MODEL_AlgBaseURL, baseUrl.toString());
		model.addAttribute(AlgorithmController.MODEL_NewAlgorithm, AlgorithmService.getNewAlgorithm());
		
		return model;
	}
	
//	public Model addAttributesToModel(Model model, ProjectModel project, Optional<Algorithm> optAlg) {
//		
//	}
	
}