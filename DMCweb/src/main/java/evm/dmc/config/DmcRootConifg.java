package evm.dmc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(evm.dmc.weka.DMCWekaConfig.class)
public class DmcRootConifg {

}
