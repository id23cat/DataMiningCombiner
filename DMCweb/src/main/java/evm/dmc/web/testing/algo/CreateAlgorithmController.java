package evm.dmc.web.testing.algo;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import evm.dmc.api.model.AlgorithmModel;
//import evm.dmc.config.ViewsConfig;
import evm.dmc.core.api.Project;
import evm.dmc.web.config.annotations.DefaultProject;
import evm.dmc.web.testing.ShowTableController;

@Controller
@RequestMapping("/testing/{userId}/project/{projId}/")
//@RequestMapping("/{userId}/createalg")
public class CreateAlgorithmController {
	private static final Logger logger = LoggerFactory.getLogger(CreateAlgorithmController.class);
	private static final String PREFIX = "testing/algorithm/";
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
	
	@GetMapping("createalg")
	String getCreateAlgorithm(@PathVariable String userId, Model model) {
		logger.debug("Inside CreateAlgorithmController: " + userId);
		
		
		AlgorithmModel algModel = project.addAlgorithm().getModel();
		model.addAttribute("algModel", algModel);
//		model.addAttribute("view", createAlgView);
		return "testing/createalg";
	}
	
	@GetMapping(value = "newalg")
	public String getAlgCreatingPage(@RequestParam("step") CreateAlgStep step) {
		logger.debug("inside getAlgCreatingPage: {}", step);

//		switch(step){
//		case DATASOURCE : return addPrefix(step.getName());
//		case FUNCTION :	return addPrefix(step.getName());
//		case DATADEST : return addPrefix(step.getName());
//		}
		
		return addPrefix(step.getName());
		
//		return "index";
	}
	
	@GetMapping("viewalg{id}")
	public String getViewAlgPage(@PathVariable String id ) {
		logger.debug("inside getViewAlgPage");
		
		return "testing/viewalg"+id;
		
	}
	
	private String addPrefix(String view) {
		return PREFIX + view;
	}
	
	@Component
	public class StringToCreateStepConverter
		implements Converter<String, CreateAlgStep> {

		@Override
		public CreateAlgStep convert(String name) {
			return CreateAlgStep.forName(name);
		}
		
	}
	
//	@Aspect
//	@Component
//	public class PrefixAppender {
//		
//		@AfterReturning(pointcut="execution(* evm.dmc.web.algo.CreateAlgorithmController.get*(..))",
//						returning="view")
//		public String addPrefix(Object view) {
//			String viewName = (String) view;
//			if(viewName.equals("index")){
//				logger.debug("inside PrefixAppender.addPrefix: {}", viewName);
//				return viewName;
//			}
//			logger.debug("inside PrefixAppender.addPrefix: {}", CreateAlgorithmController.PREFIX + viewName);
//			return CreateAlgorithmController.PREFIX + viewName;
//		}
//		
//	}


}
