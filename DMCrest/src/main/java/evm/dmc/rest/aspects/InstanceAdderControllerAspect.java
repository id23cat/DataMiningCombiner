package evm.dmc.rest.aspects;

import evm.dmc.utils.HateoasUtils;
import evm.dmc.webApi.dto.AbstractDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Defines aspect around controllers that implement instance adder
 * controller interface
 *
 * @see evm.dmc.rest.controllers.interfaces.InstanceAdderController
 */
@Aspect
@Component
@Slf4j
public class InstanceAdderControllerAspect {

    /**
     * Adds HATEOAS relation links to returned DTO
     * @param pjp Spring aspect join point
     * @param addedDto - Dto with new instance
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @return DTO with HATEOAS Relation links
     * @throws Throwable when error occurs
     */
    @Around("execution(* evm.dmc.rest.controllers.interfaces.InstanceAdderController.addInstance(..))" +
            " && args(addedDto, accountId, projectId)")
    public AbstractDto addLinksToAddedInstanceAdvice(
            ProceedingJoinPoint pjp,
            AbstractDto addedDto,
            Long accountId,
            Long projectId) throws Throwable {

        return HateoasUtils.handleDtoThroughJoinPoint(pjp, accountId, projectId);
    }

    /**
     * Adds HATEOAS relation links to returned DTO
     * @param pjp Spring aspect join point
     * @param updatedDto - Dto with updated instance
     * @param accountId Account model identifier
     * @param projectId Project model identifier
     * @return DTO with HATEOAS Relation links
     * @throws Throwable when error occurs
     */
    @Around("execution(* evm.dmc.rest.controllers.interfaces.InstanceAdderController.updateInstance(..))" +
            " && args(updatedDto, accountId, projectId)")
    public AbstractDto addLinksToUpdatedInstanceAdvice(
            ProceedingJoinPoint pjp,
            AbstractDto updatedDto,
            Long accountId,
            Long projectId) throws Throwable {

        return HateoasUtils.handleDtoThroughJoinPoint(pjp, accountId, projectId);
    }
}
