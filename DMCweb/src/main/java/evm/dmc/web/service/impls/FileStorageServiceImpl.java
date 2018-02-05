package evm.dmc.web.service.impls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.account.Account;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.config.FileStorageConfig;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.StorageFileNotFoundException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.data.DataPreview;
import evm.dmc.web.service.data.DataPreviewImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements DataStorageService {
	@Autowired
	MetaDataRepository repository;
	
    private final Path rootLocation;
    private int previewLinesCount;
    private final static String[] extensions = {"csv"};

    @Autowired
    public FileStorageServiceImpl(FileStorageConfig properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.previewLinesCount = properties.getPreviewLinesCount();
    }
    
    @Override
    @Transactional
    public MetaData save(Account account, ProjectModel project, MultipartFile file) {
    	MetaData meta = getMetaData(account, project, StringUtils.cleanPath(file.getOriginalFilename()), 
    			DataSrcDstType.LOCAL_FS, "");
    	repository.save(meta);
    	DataPreview preview;
    	try {
    		preview = store(DataStorageService.relativePath(account, project), file);
    	} catch(StorageException exc) {
    		repository.delete(meta);
    		throw exc;
    	}
    	
    	meta.setPreview(preview.getAllLines());
    	
    	return meta;
    }

    @Override
    public DataPreview store(Path relativePath, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path currLocation = this.rootLocation.resolve(relativePath);
       
        log.trace("Current location: {}", currLocation);
        // check matching any applicable extension listed in this.extensions
        Arrays.stream(extensions)
        .filter(ext -> StringUtils.getFilenameExtension(filename).toLowerCase().equals(ext))
        .findFirst().orElseThrow(() -> new UnsupportedFileTypeException(String.format("Uncknown file extension %s", filename)));
        
        try {
            Files.createDirectories(currLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
        
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            
            Path destinationPath =  currLocation.resolve(filename);
            log.debug("Saving file: {}", destinationPath);
            Files.copy(file.getInputStream(), destinationPath,
                    StandardCopyOption.REPLACE_EXISTING);
            
            return getPreview(destinationPath, this.previewLinesCount);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        
    }

    @Override
    public Stream<Path> loadAll(Path relativePath) {
    	Path currLocation = this.rootLocation.resolve(relativePath);
        try {
            return Files.walk(currLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> currLocation.relativize(path));
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(Path relativePath, String filename) {
        return rootLocation.resolve(relativePath).resolve(filename);
    }
    
    @Override
    public DataPreview loadDataPreview(Path relativePath, String filename) {
    	Path file = load(relativePath, filename);
    	return getPreview(file, previewLinesCount);
    }

    @Override
    public Resource loadAsResource(Path relativePath, String filename) {
        try {
            Path file = load(relativePath, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(Path relativePath) {
    	Path currLocation = this.rootLocation.resolve(relativePath);
        FileSystemUtils.deleteRecursively(currLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    
    private DataPreview getPreview(Path path, int linesCount) {
    	
    	String extension = StringUtils.getFilenameExtension(path.getFileName().toString()).toLowerCase();
    	
    	switch(extension) {
    		case "csv":
    			return getCsvPreview(path, linesCount);
    		
    	}
    	
    	throw new UnsupportedFileTypeException(String.format("Uncknown file extension .%s", extension));
    }
    
    private DataPreview getCsvPreview(Path path, int linesCount) {
    	DataPreview data = null;
    	try(Stream<String> stream = Files.lines(path)) {
    		List<String> lines = stream.limit(linesCount + 1).collect(Collectors.toList());	// +1 for headerLine
    		log.debug("lines count limit: {}", previewLinesCount);
    		log.debug("List of lines: {}", lines.toArray());
    		data = new DataPreviewImpl(lines);
    		
    	} catch (IOException e) {
    		throw new StorageFileNotFoundException(String.format("File %s", path.toString()), e);
    	}
    	
    	return data;
    }
    
    private DataStorageModel getDataStorageModel(String accountName, String projectName, String fileName, DataSrcDstType type) {
    	DataStorageModel storageDesc = new DataStorageModel();
    	String filename = StringUtils.cleanPath(fileName);
    	Path currLocation = this.rootLocation.resolve(DataStorageService.relativePath(accountName, projectName));
    	Path destinationPath =  currLocation.resolve(filename);
    	
    	storageDesc.setStorageType(type);
    	storageDesc.setUri(destinationPath.toUri());
    	
    	return storageDesc;
    }
    
    private MetaData getMetaData(Account account, ProjectModel project, String fileName,
    		DataSrcDstType type, String description) {
    	DataStorageModel dataStore = getDataStorageModel(account.getUserName(), project.getName(),
    			StringUtils.cleanPath(fileName), DataSrcDstType.LOCAL_FS);
	    
    	MetaData meta = new MetaData();
	    meta.setName(fileName);
	    meta.setDescription(description);
		meta.setStorage(dataStore);
		meta.setHasHeader(true);
		project.addMetaData(meta);
		
		return meta;
    }
}
