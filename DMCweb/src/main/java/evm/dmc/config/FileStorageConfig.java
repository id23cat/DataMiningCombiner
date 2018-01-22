package evm.dmc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties("fileStorage")
@Data
public class FileStorageConfig {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

}
