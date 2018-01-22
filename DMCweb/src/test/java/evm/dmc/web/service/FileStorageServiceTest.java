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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.config.FileStorageConfig;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.service.impls.FileStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
@Slf4j
public class FileStorageServiceTest {

    private FileStorageConfig properties = new FileStorageConfig();
    private FileStorageService service;
    
    private Account acccount = new Account();
	private ProjectModel project = new ProjectModel();
	
	private Path relativePath;

    @Before
    public void init() {
        properties.setLocation("target/files/" + Math.abs(new Random().nextLong()));
        service = new FileStorageServiceImpl(properties);
        service.init();
        
        acccount.setUserName("user1");
        project.setName("project1");
        
        relativePath = FileStorageService.relativePath(acccount, project);
    }
    
    @Test
    public void relativePathTest() {
    	
    	assertThat(FileStorageService.relativePath(acccount, project).toString())
    	.isEqualTo(String.format("%s/%s", acccount.getUserName(), project.getName()));
    }

    @Test
    public void loadNonExistent() {
        assertThat(service.load(relativePath, "foo.txt")).doesNotExist();
    }

    @Test
    public void saveAndLoad() {
        service.store(relativePath, new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes()));
        assertThat(service.load(relativePath, "foo.txt")).exists();
    }

    @Test(expected = StorageException.class)
    public void saveNotPermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }

    @Test
    public void savePermitted() {
        service.store(relativePath, new MockMultipartFile("foo", "bar/../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }

}
