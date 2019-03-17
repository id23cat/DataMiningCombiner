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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;

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

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.config.FileStorageConfig;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import evm.dmc.web.service.impls.FileStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles({"test", "devH2"})
@ComponentScan(basePackages = {"evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model", "evm.dmc.web.service"})
@Rollback
@Slf4j
public class DataStorageServiceTest {
    private static final int PREVIEW_LINES_COUNT = 5;
    private static final String USER_NAME = "id23cat";

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private DataPreviewService previewService;

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

    private Account account = Account.builder().build();
    private ProjectModel project;

    //	private Path relativePath;
    private String destRoot = "target/files/";
    private String srcRoot = "Data/";
    //	private String realFilename = "telecom.csv";
    private String realFilename = "telecom_churn.csv";
    private File sourceFile = new File(srcRoot, realFilename);
    private File destFile;
//	private ClassPathResource realResource = new ClassPathResource(realFilename, getClass());
//	private ClassPathResource realResource = new ClassPathResource(realFilename, getClass());

    FileStorageConfig properties;

    @Before
//    @Transactional
    public void init() {
        properties = new FileStorageConfig();
        properties.setLocation(destRoot + Math.abs(new Random().nextLong()) + File.separator);
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


        String dstRoot = properties.getLocation();
        destFile = new File(dstRoot + DataStorageService.relativePath(account, project).toString(),
                realFilename);

//        relativePath = DataStorageService.relativePath(account, project);
    }

    @Test
    public void relativePathTest() {

        assertThat(DataStorageService.relativePath(account, project).toString())
                .isEqualTo(String.format("%s/%s", account.getUserName(), project.getName()));
    }

    @Test
    public void storeAndLoadValidFile() throws IOException {

        MockMultipartFile file = new MockMultipartFile("file", realFilename,
                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(sourceFile));

        DataSetProperties dataProps = new DataSetProperties("testDataSet", "description", true);

        /**********/
        MetaData metaData = service.saveData(account, project, file, dataProps);
        assertNotNull(metaData);
        URI uri = metaData.getStorage().getUri(properties.getLocation());
        junitx.framework.FileAssert.assertEquals(sourceFile,
                new File(uri));
        log.debug("SourceFile: {}", sourceFile);
        log.debug("SourceFile size: {}", Files.size(sourceFile.toPath()));
        log.debug("DestFile: {}", destFile);
        log.debug("DestFile size: {}", Files.size(destFile.toPath()));

        DataPreview preview = previewService.getForMetaData(metaData).orElseThrow(IllegalArgumentException::new);
//        assertThat(preview.getHeader(), not(isEmptyString()));
//        log.debug("Header line: {}", preview.getHeaderItems());
//        assertThat(preview.getData().size(), equalTo(PREVIEW_LINES_COUNT));
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

    @Test
    public void deleteTest() throws FileNotFoundException, IOException {
        MockMultipartFile file = new MockMultipartFile("file", realFilename,
                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(sourceFile));
        DataSetProperties dataProps = new DataSetProperties("testDataSet", "description", true);
        service.saveData(account, project, file, dataProps);
        Set<MetaData> metaSet = metaDataService.getForProject(project);

        int initSize = metaSet.size();
        MetaData first = metaSet.stream().findFirst().get();
        metaSet = null;
//    	DataStorageModel storage = dataStorageService.getDataStorage(first);

        Set<String> names = new HashSet<>();
        names.add(first.getName());
        service.delete(project, names);


        //    	assertNull(dataStorageService.getDataStorage(first));
        assertFalse(previewService.getForMetaData(first).isPresent());

        Set<MetaData> metaSet2 = metaDataService.getForProject(project);
        assert (metaSet2.size() == initSize - 1);
        assertFalse(metaSet2.contains(first));

    }

}
