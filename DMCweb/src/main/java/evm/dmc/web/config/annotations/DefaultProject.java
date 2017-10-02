package evm.dmc.web.config.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.web.context.annotation.SessionScope;

import evm.dmc.core.api.SimplestProject;

@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER, CONSTRUCTOR })
@SimplestProject
@SessionScope
public @interface DefaultProject {

}
