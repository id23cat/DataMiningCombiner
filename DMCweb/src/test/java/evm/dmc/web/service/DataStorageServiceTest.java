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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.config.FileStorageConfig;
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
@Slf4j
public class DataStorageServiceTest {

    private FileStorageConfig properties = new FileStorageConfig();
    private DataStorageService service;
    
    private Account acccount = new Account();
	private ProjectModel project = new ProjectModel();
	
	private Path relativePath;

    @Before
    public void init() {
        properties.setLocation("target/files/" + Math.abs(new Random().nextLong()));
        properties.setPreviewLinesCount(5);
        service = new FileStorageServiceImpl(properties);
        service.init();
        
        acccount.setUserName("user1");
        project.setName("project1");
        
        relativePath = DataStorageService.relativePath(acccount, project);
    }
    
    @Test
    public void relativePathTest() {
    	
    	assertThat(DataStorageService.relativePath(acccount, project).toString())
    	.isEqualTo(String.format("%s/%s", acccount.getUserName(), project.getName()));
    }

    @Test
    public void loadNonExistent() {
        assertThat(service.load(relativePath, "foo.txt")).doesNotExist();
    }

    @Test
    public void saveAndLoadValidFile() throws IOException {
//      service.store(relativePath, new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
//      "Hello World".getBytes()));
    	String filename = "telecom.csv";
    	ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile("file", filename, 
				MediaType.TEXT_PLAIN_VALUE, resource.getInputStream());
		
		DataPreview preview = service.store(relativePath, file);
        assertThat(service.load(relativePath, "telecom.csv")).exists();
        assertNotNull(preview);
        assertThat(preview.getHeaderLine(), not(isEmptyString()));
        log.debug("Header line: {}", preview.getHeaderItems());
        assertThat(preview.getLinesCount(), equalTo(5));
        log.debug("Data lines: 0 {}", preview.getDataItems(0));
        log.debug("Data lines: 1 {}", preview.getDataItems(1));
    }
    
    @Test(expected = UnsupportedFileTypeException.class)
    public void saveAndLoadInvalidFile() throws IOException {
    	String filename = "testupload.txt";
    	ClassPathResource resource = new ClassPathResource(filename, getClass());

		MockMultipartFile file = new MockMultipartFile("file", filename,
				MediaType.TEXT_PLAIN_VALUE, resource.getInputStream());
		service.store(relativePath, file);
    }

    @Test(expected = StorageException.class)
    public void saveNotPermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }

    @Test
    public void savePermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "bar/../foo.csv",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }

}
