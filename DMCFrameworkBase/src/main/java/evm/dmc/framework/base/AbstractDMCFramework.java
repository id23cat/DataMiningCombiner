package evm.dmc.framework.base;

/**
 * Allows implementations to subscribe for framework lifecycle event.
 *
 * Not supported yet.
 */
public abstract class AbstractDMCFramework {
    protected abstract void initialize();
    protected abstract void shutdown();
}
