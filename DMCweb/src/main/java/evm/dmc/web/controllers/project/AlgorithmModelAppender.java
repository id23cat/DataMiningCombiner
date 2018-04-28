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
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.service.AlgorithmService;

@Component
//@Slf4j
public class AlgorithmModelAppender {
	@Autowired
	private AlgorithmService algorithmService;
	
	public Model addAttributesToModel(Model model, ProjectModel project) {
		Set<Algorithm> algSet = algorithmService.getForProject(project);
		model.addAttribute(AlgorithmController.MODEL_AlgorithmsSet, algSet);
				
		model.addAttribute(AlgorithmController.MODEL_NewAlgorithm, AlgorithmService.getNewAlgorithm());
		
		return setURLs(model, project);
	}
	
	public Model addAttributesToModel(Model model, ProjectModel project, Optional<Algorithm> optAlg) 
			throws AlgorithmNotFoundException {
		model = setURLs(model, project);
		if(optAlg.isPresent()){
//			model.addAttribute(AlgorithmController.MODEL_Algorithm, optAlg.get());
//			model.addAttribute(AlgorithmController.SESSION_CurrentAlgorithm, optAlg.get());
		} else {
//			return views.getErrors().getNotFound();
			throw new AlgorithmNotFoundException("No such algorithm");
		}
		return model;
	}
	
	private Model setURLs(Model model, ProjectModel project) {
		model.addAttribute(AlgorithmController.MODEL_AlgBaseURL, 
				setUriComponent(AlgorithmController.BASE_URL, project.getName()).toString());
		
		model.addAttribute(DatasetController.MODEL_DataAttributesURL, 
				setUriComponent(AlgorithmController.URL_ModifyAttributes, project.getName()).toUriString());
		
		model.addAttribute(AlgorithmController.MODEL_SelDataURL, 
				setUriComponent(AlgorithmController.URL_Select_DataSet, project.getName()).toUriString());
		
		model.addAttribute(AlgorithmController.MODEL_URL_DelAlgorithm, 
				setUriComponent(AlgorithmController.URL_Del_Algorithm, project.getName()).toUriString());
		
		return model;
	}
	
	private UriComponents setUriComponent(String uri, String component) {
		return UriComponentsBuilder.fromPath(uri).buildAndExpand(component);
	}
}