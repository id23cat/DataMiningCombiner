package evm.dmc.core.api.back;

import evm.dmc.core.api.DMCFunction;
import org.springframework.context.annotation.Scope;

@Scope("singleton")
public interface FrameworkContext {
    /**
     * Method is used for first initialization of function context or resetting
     * settings to default
     */
    void initContext();

    /**
     * Execute concrete function in context
     *
     * @param function
     */
    void executeInContext(DMCFunction function);

    // Data getNewData();

    /**
     * Pulls out value from possible environment or just copies from arg data to
     * result of function object.
     *
     * @return the value
     */
    void getValue(DMCFunction function);
}
