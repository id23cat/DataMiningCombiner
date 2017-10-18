package evm.dmc.web.testing.hello;

import org.springframework.web.bind.annotation.RestController;

import evm.dmc.config.testing.MessageConfigProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloRestController {
	private static final Logger logger = LoggerFactory.getLogger(HelloRestController.class);
	@Autowired
	MessageConfigProperties messageProperties;
    
    @RequestMapping("/testing/helloREST")
    public String greateMessage() {
        String greetingsMessage = messageProperties.getGreetings();
        logger.info("Inside index() method, returning :"+greetingsMessage);
        
//        return "Greetings from Spring Boot!";
        return greetingsMessage;
    }
    
}
