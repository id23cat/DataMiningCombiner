package evm.dmc.web.service;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.StorageFileNotFoundException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public interface DataStorageService {

    MetaData saveData(Account account, ProjectModel project, MultipartFile file, boolean hasHeader)
    	throws UnsupportedFileTypeException, StorageException;
    
    Path path(MetaData metaData);
    
    DataPreview getPreview(MetaData meta);
    
    List<String> getPreview(MetaData meta, int lineCount);

    Resource loadAsResource(MetaData metaData)
    		throws StorageFileNotFoundException;

    void deleteAll(ProjectModel project);
    
    /**
     * Combine userName from account and projectName to relative path: {userNmae}/{projectName}/
     * @param account
     * @param project
     * @return relative path that combines userName and projectNmae
     */
   static  Path relativePath(Account account, ProjectModel project) {
	   return Paths.get(account.getUserName(), project.getName());
	}
   
   static Path relativePath(String accountName, String projectName) {
	   return Paths.get(accountName, projectName);
   }
   
   
   
}
