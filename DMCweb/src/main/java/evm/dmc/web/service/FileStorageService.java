package evm.dmc.web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public interface FileStorageService {

    void init();

    void store(Path relativePath, MultipartFile file);

    Stream<Path> loadAll(Path relativePath);

    Path load(Path relativePath, String filename);

    Resource loadAsResource(Path relativePath, String filename);

    void deleteAll(Path relativePath);
    
    /**
     * Combine userName from account and projectName to relative path: {userNmae}/{projectName}/
     * @param account
     * @param project
     * @return relative path that combines userName and projectNmae
     */
   static  Path relativePath(Account account, ProjectModel project) {
	   return Paths.get(account.getUserName(), project.getName());
	}

}
