package evm.dmc.framework.base;

import evm.dmc.framework.base.discovery.annotations.DMCFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Wraps the binding between function method and framework object.
 *
 */
public class DMCFunctionInvoker {
    private Object functionObject;
    private Method functionMethod;

    public DMCFunctionInvoker(Object functionObject, Method functionMethod) {
        this.functionObject = functionObject;
        this.functionMethod = functionMethod;
    }

    /**
     * Calls wrapped function. It's caller responsibility to ensure, that
     * function arguments was specified correctly and return type will be
     * as expected.
     *
     * @param args                          function argument list
     * @return                              function invocation result
     * @throws InvocationTargetException    see {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException       see {@link Method#invoke(Object, Object...)}
     */
    public Object call(Object ... args) throws InvocationTargetException, IllegalAccessException {
        return functionMethod.invoke(functionObject, args);
    }
}
