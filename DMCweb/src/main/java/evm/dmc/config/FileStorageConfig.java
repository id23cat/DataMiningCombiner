package evm.dmc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;

@Configuration
@Data
public class FileStorageConfig {

    /**
     * Folder location for storing files
     */
	@Value("${dmc.datastorage.root: upload-dir}")
	private String location = "upload-dir";
	
	@Value("${dmc.datastorage.preview.linesCount: 7}")
	private int previewLinesCount = 5;
	
	@Value("${dmc.datastorage.preview.retriesCount: 3}")
	private int retriesCount = 3;
	
	@Value("${dmc.datastorage.preview.fileWaitTimeoutMS: 100}")
	private long fileWaitTimeoutMS;

}
