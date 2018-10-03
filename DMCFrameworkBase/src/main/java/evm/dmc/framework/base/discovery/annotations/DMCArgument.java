package evm.dmc.framework.base.discovery.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines argument of some DMC function.
 *
 * Can be used to override java argument name.
 *
 * @see DMCFunction
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DMCArgument {
    /**
     * Name of parameter that will be shown to end user.
     *
     * @return  name of parameter
     */
    String value();
}
