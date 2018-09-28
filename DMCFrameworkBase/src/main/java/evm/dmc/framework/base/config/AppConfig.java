package evm.dmc.framework.base.config;

import evm.dmc.framework.base.discovery.annotations.DMCFramework;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Scans for all frameworks, available on classpath.
 *
 * @see evm.dmc.framework.base.discovery.FrameworkDiscoveryContainer
 */
@Configuration
@ComponentScan(
    basePackages = {"evm.dmc.framework"},
    includeFilters = {
        @ComponentScan.Filter(classes = {DMCFramework.class})
    }
)
public class AppConfig {
}