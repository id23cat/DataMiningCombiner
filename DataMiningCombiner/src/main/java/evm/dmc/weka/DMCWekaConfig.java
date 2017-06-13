package evm.dmc.weka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import evm.dmc.core.DMCCoreConfig;

@Configuration
@ComponentScan("evm.dmc.weka")
@Import(DMCCoreConfig.class)
@PropertySource("classpath:weka.properties")
public class DMCWekaConfig {

}
