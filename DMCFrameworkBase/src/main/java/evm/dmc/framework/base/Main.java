package evm.dmc.framework.base;

import evm.dmc.framework.base.config.AppConfig;
import evm.dmc.framework.base.discovery.FrameworkDiscoveryContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        applicationContext.registerShutdownHook();

        FrameworkDiscoveryContainer container = applicationContext.getBean(FrameworkDiscoveryContainer.class);
        DMCFunctionInvoker invoker = container.getFunction("Example", "hellower", "world");

        System.out.println(invoker.call("World!"));
    }
}
