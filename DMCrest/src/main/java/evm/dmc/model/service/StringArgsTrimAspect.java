package evm.dmc.model.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class StringArgsTrimAspect {
//	@Pointcut("execution(* evm.dmc.web.service.impls.*(..)) && args(.., String)")
//	@Pointcut("execution(* evm.dmc.web.service.impls.AlgorithmServiceImpl.*(..))")
//	public void trimString() {}
	
//	@Around("execution(* evm.dmc.web.service.impls.AlgorithmServiceImpl.*(..))")
	@Around("within(evm.dmc.web.service.impls.*)")
	public Object trimStringAdice(final ProceedingJoinPoint pjp) throws Throwable {
		Object[] arguments = pjp.getArgs();
		for(int i=0; i < arguments.length; i++) {
			Object object = arguments[i];
			if(object instanceof String) {
				log.debug("-== Aspect trim: {}", (String)object);
				arguments[i] = ((String)object).trim();
			}
		}
		return pjp.proceed(arguments);
	}

}
