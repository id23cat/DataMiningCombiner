package evm.dmc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.api.model.account.Account;
import evm.dmc.utils.MessageHelper;
import evm.dmc.web.exceptions.UserNotExistsException;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;
import evm.dmc.web.service.AccountService;

@Controller
@RequestMapping
public class RegisterController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private Views views;
	
	private String userExistsMessage = "User with same name or email already exists";
	
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
			Errors errors, RedirectAttributes ra) throws UserNotExistsException{
		if(errors.hasErrors()) {
			
			logger.debug("Invalid registration attempt: {}", errors);
			return views.getRegister();
		}
		
		account = accountService.save(account);
		MessageHelper.addSuccessAttribute(ra, "registration success");
		return "redirect:/";
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView handleDataIntegrityViolation(HttpServletRequest req, 
							DataIntegrityViolationException exception) throws Exception {
		logger.debug("DataIntegrityViolationException catched");
		logger.debug("Cause: {}", ExceptionUtils.getRootCause(exception).getMessage());
		if(ExceptionUtils.getRootCause(exception).getMessage().contains("Unique index or primary key violation")){
			ModelAndView mav = new ModelAndView(views.getRegister());
			mav.addObject(new Account());
			mav.addObject("exception", ExceptionUtils.getRootCause(exception));
			mav.addObject("errorMessage", userExistsMessage);
			logger.debug("Catched UserExistsException");
			return mav;
		}
		throw exception;
		
	}
	
	
	
}