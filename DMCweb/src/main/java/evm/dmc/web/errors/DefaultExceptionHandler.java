package evm.dmc.web.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.google.common.base.Throwables;

import evm.dmc.web.SignInController;

/**
 * General error handler for the application.
 */
@ControllerAdvice
public class DefaultExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger("ErrorLog");
	/**
	 * Handle exceptions thrown by handlers.
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
	public String processConstraintError(DataIntegrityViolationException ex) {
		return "userExists";
	}
//	
//	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
//	public ModelAndView exception(Exception exception, WebRequest request) {
//		ModelAndView modelAndView = new ModelAndView("error/general");
//        Throwable rootCause = Throwables.getRootCause(exception);
//        modelAndView.addObject("errorMessage", rootCause);
//        LOGGER.error(rootCause.toString(), exception);
//		return modelAndView;
//	}
	
//	@ExceptionHandler
//	public ModelAndView exception(Exception exc, WebRequest request) {
//		if(exc instanceOf )
//		ModelAndVeiw modelAndView = new ModelAndView("errors")
//	}

}
