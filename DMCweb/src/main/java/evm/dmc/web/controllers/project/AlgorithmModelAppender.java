package evm.dmc.web.controllers.project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.algorithm.Algorithm;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.web.exceptions.AlgorithmNotFoundException;
import evm.dmc.web.service.AlgorithmService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.JsonService;
import evm.dmc.web.service.dto.TreeNodeDTO;

@Component
//@Slf4j
public class AlgorithmModelAppender {
	@Autowired
	private AlgorithmService algorithmService;
	
	@Autowired
	private JsonService jsonService;
	
	@Autowired DataStorageService dataStorageService;
	
	public Model addAttributesToModel(Model model, ProjectModel project) {
		List<Algorithm> algList = algorithmService.getForProjectSortedBy(project, "name");
		model.addAttribute(AlgorithmController.MODEL_AlgorithmsList, algList);
				
		model.addAttribute(AlgorithmController.MODEL_NewAlgorithm, AlgorithmService.getNewAlgorithm(project));
		
		return setURLs(model, project);
	}
	
	public Model addAttributesToModel(Model model, ProjectModel project, Optional<Algorithm> optAlg) 
			throws AlgorithmNotFoundException, JsonProcessingException {
		model = setURLs(model, project);
		if(optAlg.isPresent()){
//			if(optAlg.get().getDataSource() != null) {
//				model.addAttribute(DatasetController.MODEL_MetaData, 
//						algorithmService.getDataSource(optAlg.get()));
//				model.addAttribute(DatasetController.MODEL_Preview,
//						dataStorageService.getPreview(optAlg.get().getDataSource()));
//			}
			Optional<MetaData> optSource = algorithmService.getDataSource(optAlg);
			if(optSource.isPresent()) {
				model.addAttribute(DatasetController.MODEL_MetaData, optSource.get());
				model.addAttribute(DatasetController.MODEL_Preview, 
						dataStorageService.getPreview(optSource.get()));
			}
			
//			model.addAttribute(AlgorithmController.MODEL_Algorithm, optAlg.get());
//			model.addAttribute(AlgorithmController.SESSION_CurrentAlgorithm, optAlg.get());
			
			List<TreeNodeDTO> functionsListDTO = algorithmService.getFrameworksAsTreeNodes();
			model.addAttribute(AlgorithmController.MODEL_FunctionsList, jsonService.frameworksListToTreeView(functionsListDTO));
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
		
		model.addAttribute(AlgorithmController.MODEL_URL_SelectFunction,
				setUriComponent(AlgorithmController.URL_Select_Function, project.getName()).toUriString());
		
		return model;
	}
	
	private UriComponents setUriComponent(String uri, String component) {
		return UriComponentsBuilder.fromPath(uri).buildAndExpand(component);
	}
}