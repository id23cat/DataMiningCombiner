package evm.dmc.web.testing;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.core.api.Data;
import evm.dmc.core.api.Framework;
import evm.dmc.core.api.back.CSVLoader;
import evm.dmc.core.services.FrameworksService;
import evm.dmc.service.testing.TestingViewsService;
import evm.dmc.weka.function.WekaFunctions;

//@Controller
//// @RequestMapping("/user/{userId}/showtable")
//@RequestMapping("/testing/showtable")
//public class ShowTableController {
//	private static final Logger logger = LoggerFactory.getLogger(ShowTableController.class);
//	Data<?> data;
//
//	// @Value("${wekatest.datasource}")
//	String sourceFileName = "Data/telecom_churn.csv";
//	
//	TestingViewsService views;
//
//	Set<String> names;
//
//	@Autowired
//	ShowTableController( FrameworksService repository, TestingViewsService vservice) {
//		CSVLoader loader = (CSVLoader) repository.getFunction(WekaFunctions.CSVLOADER);
//		this.data = loader.setSource(sourceFileName).get();
//
//		Set<String> frmwksDesc = repository.getFrameworksDescriptors();
//		assert(!frmwksDesc.isEmpty());
//		logger.debug(frmwksDesc.iterator().next());
//		Framework wekaFramework = repository.getFramework(frmwksDesc.iterator().next());
//		names = wekaFramework.getFunctionDescriptors();
//		logger.debug("Framework Ref {} ", wekaFramework.toString());
//		logger.debug("Avaliable beans{} ", this.names);
//		
//		views = vservice;
//	}
//
//	@GetMapping("/table/{tableId}")
//	public String showTable(@PathVariable String tableId, Model model) {
//		logger.debug("showTable method args: {}", tableId);
//		 model.addAttribute("data", data.getAllAsString());
//
//		return "testing/showtable";
//
//	}
//
//	@RequestMapping("/listbeans")
//	public String listBeans(Model model) {
////		model.addAttribute("data", data.getAllAsString());
//		model.addAttribute("namesList", names);
//		// model.addAllAttributes(this.names);
//		return "testing/showtable";
//
//	}
//
//}
