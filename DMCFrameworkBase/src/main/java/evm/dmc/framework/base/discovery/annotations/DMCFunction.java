package evm.dmc.framework.base.discovery.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static evm.dmc.framework.base.Constants.FUNCTION_DEFAULT_NAME;

/**
 * Defines function as implementation of DMC function. It will be called by
 * reflection during runtime.
 *
 * Functions has following requirements:
 * <ul>
 *     <li>Function MUST reside in the framework class, to which it belongs</li>
 *     <li>Function MUST expose public access</li>
 * </ul>
 *
 * @see DMCFramework
 * @see DMCArgument
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DMCFunction {
    /**
     * Name of function, that will be shown to the end user.
     *
     * @return  name of function, java name if empty
     */
    String value() default FUNCTION_DEFAULT_NAME;
}
