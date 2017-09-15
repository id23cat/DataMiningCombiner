package evm.dmc.weka;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import evm.dmc.core.annotations.FW;

@Target(value = { java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR,
		java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD,
		java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@FW("WekaFramework")
public @interface WekaFW {

}
