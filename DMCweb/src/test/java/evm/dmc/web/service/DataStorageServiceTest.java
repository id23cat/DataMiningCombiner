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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import org.junit.Before;
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
import evm.dmc.config.FileStorageConfig;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import evm.dmc.web.service.data.DataPreview;
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
    
    @Autowired
    private	MetaDataRepository metaDataRepository;
    
    private DataStorageService service;
    
//    @Autowired
//    private TestEntityManager entityManager;
    
    @Autowired
    AccountRepository accRepo;
    
    @Autowired
    ProjectService projectService;
    
    
    private Account account = new Account();
	private ProjectModel project = new ProjectModel();
	
	private Path relativePath;
	private String realFilename = "telecom.csv";
	private ClassPathResource realResource = new ClassPathResource(realFilename, getClass());
	
	private static final int PREVIEW_LINES_COUNT = 5;

    @Before
    @Transactional
    public void init() {
    	FileStorageConfig properties = new FileStorageConfig();
		properties.setLocation("target/files/" + Math.abs(new Random().nextLong()));
        properties.setPreviewLinesCount(PREVIEW_LINES_COUNT);
        
        service = new FileStorageServiceImpl(properties, metaDataRepository, projectService);
        
        account = accRepo.findByUserName("id23cat").get();
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
    public void loadNonExistent() {
        assertThat(service.load(relativePath, "foo.txt")).doesNotExist();
    }

    @Test
    public void storeAndLoadValidFile() throws IOException {
//      service.store(relativePath, new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
//      "Hello World".getBytes()));
//    	String filename = "telecom.csv";
//    	ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile("file", realFilename, 
				MediaType.TEXT_PLAIN_VALUE, realResource.getInputStream());
		
		DataPreview preview = service.store(relativePath, file);
        assertThat(service.load(relativePath, realFilename)).exists();
        assertNotNull(preview);
        assertThat(preview.getHeaderLine(), not(isEmptyString()));
        log.debug("Header line: {}", preview.getHeaderItems());
        assertThat(preview.getLinesCount(), equalTo(5));
        log.debug("Data lines: 0 {}", preview.getDataItems(0));
        log.debug("Data lines: 1 {}", preview.getDataItems(1));
    }
    
    @Test(expected = UnsupportedFileTypeException.class)
    public void storeAndLoadInvalidFile() throws IOException {
    	String filename = "testupload.txt";
    	ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile("file", filename,
				MediaType.TEXT_PLAIN_VALUE, resource.getInputStream());
		service.store(relativePath, file);
    }

    @Test(expected = StorageException.class)
    public void storeNotPermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }

    @Test
    public void storePermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "bar/../foo.csv",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }
    
    @Test
    public void testSave() throws IOException {
    	MockMultipartFile file = new MockMultipartFile("file", realFilename, 
				MediaType.TEXT_PLAIN_VALUE, realResource.getInputStream());
    	
    	MetaData meta = service.saveData(account, project, file);
    	assertNotNull(meta);
    	assertNotNull(meta.getId());
    	assertThat(meta.getPreview(), not(empty()));
    	assertThat(meta.getPreview().size(), equalTo(PREVIEW_LINES_COUNT+1)); // header line + data lines 
    	assertThat(meta.getDataLinesCount(), equalTo(PREVIEW_LINES_COUNT));
    	assertThat(meta.getAttributes().get("State").getLines(), contains("KS", "OH", "NJ", "OH", "OK"));
    }

}
