/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package evm.dmc.web.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.assertj.core.api.FileAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.config.FileStorageConfig;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import evm.dmc.web.service.impls.FileStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Dave Syer
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model", "evm.dmc.web.service"})
@Rollback
@Slf4j
public class DataStorageServiceTest {
	private static final int PREVIEW_LINES_COUNT = 5;
	private static final String USER_NAME = "id23cat"; 
    
    @Autowired
    private	MetaDataService metaDataService;
    
    @Autowired
    DataPreviewService previewService;
    
//    @Autowired
    private DataStorageService service;
    
//    @Autowired
//    private TestEntityManager entityManager;
    
    @Autowired
    private AccountRepository accRepo;
    
    @Autowired
    private ExecutorService executorService;
    
    @Autowired
    private ProjectService projectService;
    
    private Account account = new Account();
	private ProjectModel project = new ProjectModel();
	
	private Path relativePath;
	private String destRoot = "target/files/";
//	private String realFilename = "telecom.csv";
	private String realFilename = "telecom_churn.csv";
	private ClassPathResource realResource = new ClassPathResource(realFilename, getClass());
	
	FileStorageConfig properties;
	
    @Before
//    @Transactional
    public void init() {
//    	try {
//			Files.createFile(Paths.get("111.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	properties = new FileStorageConfig();
		properties.setLocation(destRoot + Math.abs(new Random().nextLong())+File.separator);
        properties.setPreviewLinesCount(PREVIEW_LINES_COUNT);
        properties.setRetriesCount(3);
        properties.setFileWaitTimeoutMS(500);
        
        service = new FileStorageServiceImpl(properties, metaDataService, executorService, projectService);
        
        account = accRepo.findByUserName(USER_NAME).get();
        assertNotNull(account);
//        account.setUserName("user1");
//        project.setName("project1");
        project = account.getProjects().stream().findFirst().get();
        assertNotNull(project);
        
        relativePath = DataStorageService.relativePath(account, project);
    }
    
    @Test
    public void relativePathTest() {
    	
    	assertThat(DataStorageService.relativePath(account, project).toString())
    	.isEqualTo(String.format("%s/%s", account.getUserName(), project.getName()));
    }

    @Test
    public void storeAndLoadValidFile() throws IOException {
    	
    	MetaData metaData = null;
    	File sourceFile = realResource.getFile();
    	File destFile = new File(properties.getLocation() + 
    			DataStorageService.relativePath(account, project).toString(),
    			realFilename);
//		for(int i=0; i<50; i++) {
			MockMultipartFile file = new MockMultipartFile("file", realFilename, 
					MediaType.TEXT_PLAIN_VALUE, realResource.getInputStream());
			
			metaData = service.saveData(account, project, file, null);
//		}
		assertNotNull(metaData);
		junitx.framework.FileAssert.assertEquals(sourceFile, new File(metaData.getStorage().getUri()));
		log.debug("SourceFile: {}", sourceFile);
		log.debug("SourceFile size: {}", Files.size(sourceFile.toPath()));
		log.debug("DestFile: {}", destFile);
		log.debug("DestFile size: {}", Files.size(destFile.toPath()));

        DataPreview preview = previewService.getForMetaData(metaData).orElseThrow( IllegalArgumentException::new);
        assertThat(preview.getHeader(), not(isEmptyString()));
//        log.debug("Header line: {}", preview.getHeaderItems());
        assertThat(preview.getData().size(), equalTo(PREVIEW_LINES_COUNT));
//        log.debug("Data lines: 0 {}", preview.getDataItems(0));
//        log.debug("Data lines: 1 {}", preview.getDataItems(1));
    }
    
    @Test(expected = UnsupportedFileTypeException.class)
    public void storeAndLoadInvalidFile() throws IOException {
    	String filename = "testupload.txt";
    	ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile("file", filename,
				MediaType.TEXT_PLAIN_VALUE, resource.getInputStream());
		
		service.saveData(account, project, file, null);
    }

    @Test(expected = StorageException.class)
    public void storeNotPermitted() {
        service.saveData(account, project, new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()), null);
    }

    

}
