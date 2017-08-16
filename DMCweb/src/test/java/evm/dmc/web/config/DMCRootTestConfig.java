package evm.dmc.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({ "evm.dmc.core",  })
@Import({evm.dmc.core.DMCCoreConfig.class })
public class DMCRootTestConfig {

}
