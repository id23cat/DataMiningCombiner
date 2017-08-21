package evm.dmc.web.algo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.core.api.FrameworksRepository;

@Controller
@RequestMapping("/{userId}/newalg")
public class NewAlgorithm {
	@Autowired
	FrameworksRepository repository;
	
//	@GetMappting
//	String getData()

}
