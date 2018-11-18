package evm.dmc.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Helps to pick up information about relation name needed for
 * HATEOAS REST API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HateoasRelation {

     /**
      * @return name of relation provided by method
      */
     String value();
}
