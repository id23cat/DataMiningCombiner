package evm.dmc.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.service.RequestPath;
import evm.dmc.service.Views;

@Controller
@RequestMapping(RequestPath.home)
public class HomeController {
	
	@Autowired
	Views views;
	
	@GetMapping
	public String getHome(Authentication authentication) {
		if(authentication != null && authentication.isAuthenticated())
			return "redirect:" + RequestPath.project;
		else
			return views.getIndex();
		
	}

}
