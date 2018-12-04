package evm.dmc.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Helps to pick up information about child classes needed for
 * HATEOAS REST API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HateoasRelationChildren {

    /**
     * @return array of child classes related to target class
     */
    Class<?>[] value();
}
