package evm.dmc.framework.base;

import evm.dmc.framework.base.discovery.annotations.DMCArgument;
import evm.dmc.framework.base.discovery.annotations.DMCFramework;
import evm.dmc.framework.base.discovery.annotations.DMCFunction;

// TODO: create tests
@DMCFramework("Example")
public class ExampleFramework {

    @DMCFunction("hellower")
    public String sayHello(
        @DMCArgument("world") String s
    ) {
        return "Hello " + s;
    }
}
