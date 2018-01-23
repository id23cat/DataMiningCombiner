package evm.dmc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class FileStorageConfig {

    /**
     * Folder location for storing files
     */
	@Value("${dmc.fileroot: upload-dir}")
    private String location = "upload-dir";

}
