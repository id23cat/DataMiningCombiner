package evm.dmc.web.controllers.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project+"/{projectName}"+RequestPath.dataset)
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class DatasetController {
	@Autowired
	private DataStorageService metaDataService;
	
	@Autowired
	private Views views;
	
//	@GetMapping(RequestPath.getAll)
//	public String getDatasetsList() {
//		
//	}
}
