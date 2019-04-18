package evm.dmc.model.service;

import org.springframework.web.multipart.MultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.webApi.exceptions.StorageException;
import evm.dmc.webApi.exceptions.UnsupportedFileTypeException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public interface DataStorageService {

    MetaData saveData(Account account, ProjectModel project, MultipartFile file, DataSetProperties datasetProperties)
            throws UnsupportedFileTypeException, StorageException;

//    MetaData save(MetaData metaData);
//    
//    Path path(MetaData metaData);

    DataPreview getPreview(MetaData meta);

//    List<String> getPreview(MetaData meta, int lineCount);

    DataStorageModel getDataStorage(MetaData meta);

//    Resource loadAsResource(MetaData metaData)
//    		throws StorageFileNotFoundException;

    void delete(ProjectModel project, Set<String> names);

    void deleteAll(ProjectModel project);

    /**
     * Combine userName from account and projectName to relative path: {userNmae}/{projectName}/
     *
     * @param account
     * @param project
     * @return relative path that combines userName and projectNmae
     */
    static Path relativePath(Account account, ProjectModel project) {
        return Paths.get(account.getUserName(), project.getName());
    }

    static Path relativePath(String accountName, String projectName) {
        return Paths.get(accountName, projectName);
    }


}
