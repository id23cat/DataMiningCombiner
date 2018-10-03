package evm.dmc.framework.base.discovery.annotations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

import static evm.dmc.framework.base.Constants.FRAMEWORK_BEAN_QUALIFIER;
import static evm.dmc.framework.base.Constants.FRAMEWORK_DEFAULT_NAME;

/**
 * Defines class as implementation of DMC framework.
 *
 * All framework function declarations MUST reside inside framework class.
 * You can use multiple framework classes with same name.
 *
 * @see DMCFunction
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Qualifier(FRAMEWORK_BEAN_QUALIFIER)
public @interface DMCFramework {
    /**
     * Name of framework, that will be shown to end user. MUST be
     * unique globally.
     *
     * @return  name of framework, java name if empty
     */
	String value() default FRAMEWORK_DEFAULT_NAME;
}
