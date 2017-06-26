package evm.dmc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.core.function.CSVLoader;
import evm.dmc.weka.WekaFW;
import evm.dmc.weka.data.WekaData;
import evm.dmc.weka.function.WekaFunctions;

@Controller
@RequestMapping("/user/{userId}/showtable")
public class ShowTableController {
	WekaData data;
	
	@Value("${wekatest.datasource}")
	String sourceFileName;
	
	ShowTableController(@Autowired @WekaFW WekaFramework frmwk){
		CSVLoader loader = (CSVLoader) frmwk.getDMCFunction(WekaFunctions.CSVLOADER);
		this.data = ((WekaData) loader.setSource(sourceFileName).get());
	}
	
	
	@GetMapping("/{tabeId}")
	public String showTable(
			@PathVariable String userId,
			@PathVariable String tableId, 
			Model model){
//				model.addAttribute("data", data.getAllAsString());
				return "showtable";
		
	}

}
