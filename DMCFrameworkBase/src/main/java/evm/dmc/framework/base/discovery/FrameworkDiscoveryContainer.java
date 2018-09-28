package evm.dmc.framework.base.discovery;

import evm.dmc.framework.base.DMCFunctionInvoker;
import evm.dmc.framework.base.discovery.annotations.DMCArgument;
import evm.dmc.framework.base.discovery.annotations.DMCFramework;
import evm.dmc.framework.base.discovery.annotations.DMCFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static evm.dmc.framework.base.Constants.FRAMEWORK_BEAN_QUALIFIER;
import static evm.dmc.framework.base.Constants.FRAMEWORK_DEFAULT_NAME;

/**
 * Contains all discovered frameworks and their discoveredFunctions.
 */
@Component
public class FrameworkDiscoveryContainer implements InitializingBean {
    public static final Logger logger = LoggerFactory.getLogger(FrameworkDiscoveryContainer.class);

    @Autowired
    @Qualifier(FRAMEWORK_BEAN_QUALIFIER)
    private List<Object> discoveredFrameworks;

    private Map<String, DMCFunctionInvoker> discoveredFunctions = new HashMap<>();

    /**
     * Initializes function map after completion of framework discovery.
     */
    @Override
    public void afterPropertiesSet() {
        for (Object frameworkObject : discoveredFrameworks) {
            Method[] methods = frameworkObject.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Annotation dmcFunction = method.getAnnotation(DMCFunction.class);
                if (dmcFunction != null) {
                    DMCFunctionInvoker functionInvoker = new DMCFunctionInvoker(frameworkObject, method);
                    discoveredFunctions.put(getFunctionHash(frameworkObject, method), functionInvoker);
                }
            }
        }
    }

    /**
     * Allows to retrieve discovered framework function
     *
     * @param frameworkName     framework name
     * @param functionName      function name
     * @param argumentsNames    arguments names
     * @return                  {@link DMCFunctionInvoker}
     */
    public DMCFunctionInvoker getFunction(String frameworkName,
                                          String functionName,
                                          String ... argumentsNames) {
        return discoveredFunctions.get(getFunctionHash(frameworkName, functionName, argumentsNames));
    }

    private String getFrameworkName(Class<?> framework) {
        DMCFramework frameworkAnnotation = framework.getDeclaredAnnotation(DMCFramework.class);
        if (frameworkAnnotation == null) {
            throw new IllegalArgumentException(
                "Framework class MUST have @DMCFramework annotation! Class name: "
                + framework.getCanonicalName()
            );
        }

        if (FRAMEWORK_DEFAULT_NAME.equals(frameworkAnnotation.value())) {
            return framework.getSimpleName();
        } else {
            return frameworkAnnotation.value();
        }
    }

    private String getFunctionName(Method function) {
        DMCFunction functionAnnotation = function.getDeclaredAnnotation(DMCFunction.class);
        if (functionAnnotation != null) {
            return functionAnnotation.value();
        } else {
            return function.getName();
        }
    }

    private String getArgumentName(Parameter argument) {
        DMCArgument argumentAnnotation = argument.getDeclaredAnnotation(DMCArgument.class);
        if (argumentAnnotation != null) {
            return argumentAnnotation.value();
        } else {
            return argument.getName();
        }
    }

    private String getFunctionHash(Object framework, Method functionMethod ) {
        String frameworkName = getFrameworkName(framework.getClass());
        String functionName = getFunctionName(functionMethod);

        Parameter[] functionArguments = functionMethod.getParameters();
        String[] argumentsNames = new String[functionArguments.length];
        for (int i = 0; i < argumentsNames.length; i++) {
            argumentsNames[i] = getArgumentName(functionArguments[i]);
        }

        return getFunctionHash(frameworkName, functionName, argumentsNames);
    }

    // TODO use something more elegant
    private String getFunctionHash(String frameworkName, String functionName, String ... argumentsNames) {
        logger.trace("Called with {}, {}, {}", frameworkName, functionName, Arrays.toString(argumentsNames));
        return String.valueOf((
                frameworkName
                + functionName
                + Arrays.toString(argumentsNames)
            ).hashCode());
    }
}
