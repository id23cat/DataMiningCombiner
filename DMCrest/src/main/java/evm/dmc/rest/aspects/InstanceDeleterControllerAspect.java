package evm.dmc.rest.aspects;

import evm.dmc.utils.HateoasUtils;
import evm.dmc.webApi.dto.AbstractDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Defines aspect around controllers that implement instance getter
 * controller interface
 *
 * @see evm.dmc.rest.controllers.interfaces.InstanceGetterController
 */
@Aspect
@Component
@Slf4j
public class InstanceDeleterControllerAspect {

    /**
     * Adds HATEOAS relation links to returned DTO
     *
     * @param pjp       Spring aspect join point
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @param entityId  Dto identifier
     * @return DTO with HATEOAS Relation links
     * @throws Throwable when error occurs
     */
    @Around("execution(* evm.dmc.rest.controllers.interfaces.InstanceDeleterController.deleteInstance(..))" +
            " && args(accountId, projectId, entityId)")
    public AbstractDto addLinksToDeletedInstanceAdvice(
            ProceedingJoinPoint pjp,
            Long accountId,
            Long projectId,
            Long entityId) throws Throwable {

        return HateoasUtils.handleDtoThroughJoinPoint(pjp, accountId, projectId);
    }
}
