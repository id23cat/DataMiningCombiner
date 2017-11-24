package evm.dmc.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.business.account.Account;
import evm.dmc.business.account.AccountService;
import evm.dmc.service.RequestPath;
import evm.dmc.service.Views;
import evm.dmc.utils.MessageHelper;

@Controller
@RequestMapping
public class RegisterController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private Views views;
	
	
	
	@ModelAttribute
	public Account setAccountToModel() {
		return new Account();
	}

	@GetMapping(value={RequestPath.register})
	public String getRegistrationPage(Model model){
		logger.debug(" ");		
		return views.getRegister();
	}
	
	@PostMapping(RequestPath.register)
	public String register(@Valid @ModelAttribute Account account, 
			Errors errors, RedirectAttributes ra){
		if(errors.hasErrors()) {
			
			logger.debug("Invalid registration attempt: {}", errors);
			return views.getRegister();
		}
		
		account = accountService.save(account);
		MessageHelper.addSuccessAttribute(ra, "registration success");
		return "redirect:/";
	}
	
}