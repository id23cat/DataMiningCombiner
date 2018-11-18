package evm.dmc.rest.aspect;

import evm.dmc.util.HateoasUtils;
import evm.dmc.webApi.dto.AbstractDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines aspect around controllers that implement instance getter
 * controller interface
 *
 * @see evm.dmc.rest.controllers.interfaces.InstanceGetterController
 */
@Aspect
@Component
@Slf4j
public class InstanceGetterControllerAspect {

    /**
     * Adds HATEOAS relation links to returned DTO
     * @param pjp Spring aspect join point
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @return DTO with HATEOAS Relation links
     * @throws Throwable when error occurs
     */
    @Around("execution(* evm.dmc.rest.controllers.interfaces.InstanceGetterController.getInstance(..))" +
            " && args(accountId, projectId, entityId)")
    public AbstractDto addLinksToInstanceAdvice(
            ProceedingJoinPoint pjp,
            Long accountId,
            Long projectId,
            Long entityId) throws Throwable {

        Class<?> clazz = pjp.getSignature().getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method calledMethod = methodSignature.getMethod();

        AbstractDto dto = (AbstractDto) pjp.proceed();
        HateoasUtils.addBasicLinkSetToDto(dto, clazz, calledMethod, accountId, projectId);

        return dto;
    }

    /**
     * Adds HATEOAS relation links to returned DTO list
     * @param pjp Spring aspect join point
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @return list of DTOs with HATEOAS Relation links
     * @throws Throwable when error occurs
     */
    @Around("execution(* evm.dmc.rest.controllers.interfaces.InstanceGetterController.getInstanceList(..))" +
            " && args(accountId, projectId)")
    public List<AbstractDto> addLinksToInstanceListAdvice(
            ProceedingJoinPoint pjp,
            Long accountId,
            Long projectId) throws Throwable {

        Class<?> clazz = pjp.getSignature().getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method calledMethod = methodSignature.getMethod();

        List dtoList = (List) pjp.proceed();
        List<AbstractDto> proceedDtoList = new ArrayList<>(dtoList.size());
        for(Object dto : dtoList) {
            AbstractDto proceedDto = HateoasUtils.addBasicLinkSetToDto(
                    (AbstractDto) dto, clazz, calledMethod, accountId, projectId);
            proceedDtoList.add(proceedDto);
        }

        return proceedDtoList;
    }
}
