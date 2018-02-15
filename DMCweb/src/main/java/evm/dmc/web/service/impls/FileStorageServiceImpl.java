package evm.dmc.web.service.impls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
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
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.config.FileStorageConfig;
import evm.dmc.core.api.AttributeType;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.model.repositories.MetaDataRepository;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.StorageFileNotFoundException;
import evm.dmc.web.exceptions.UnsupportedFileTypeException;
import evm.dmc.web.service.DataPreviewService;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements DataStorageService {
	@Autowired 
	FileStorageConfig properties;
	
	@Autowired
	private ExecutorService executorService;
	
	@Autowired
	private MetaDataService metaDataService;
	
    private Path rootLocation;
    
    private int previewLinesCount;
    
   
    private final static String[] EXTENSIONS = {"csv"};
    

    @Autowired
    public FileStorageServiceImpl(FileStorageConfig properties, MetaDataService metaDataService,
    		ExecutorService executorService) {
    	this.properties = properties;
    	this.metaDataService = metaDataService;
    	this.executorService = executorService;
    	
        init();
    }
    
//    @Override
//    @PostConstruct
    private void init() {
    	this.rootLocation = Paths.get(properties.getLocation());
    	this.previewLinesCount = properties.getPreviewLinesCount();
    	
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    
    protected Runnable storeData(Path destinationPath, MultipartFile file)
    	throws StorageException {
    	return new Runnable() {
    		private Path destinationPath;
    		private MultipartFile file;
    		Runnable init(Path destinationPath, MultipartFile file) {
    			this.destinationPath = destinationPath;
    			this.file = file;
    			return this;
    		}

			@Override
			public void run() {
		            log.debug("Saving file: {}", destinationPath);
		            
		            // saving
		            try {
						Files.copy(file.getInputStream(), destinationPath,
						        StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						throw new StorageException("Failed to save file " + destinationPath, e);
					}
				
			}
    	}.init(destinationPath, file);
    }
    
   
    
    @Override
    @Transactional
    public MetaData saveData(Account account, ProjectModel project, MultipartFile file) 
    		throws UnsupportedFileTypeException, StorageException {
    	// 1. Check filepath, extension and non-empty data 
    	checkFile(file);	// throws UnsupportedFileTypeException, StorageException
    	
    	
    	String filename = StringUtils.cleanPath(file.getOriginalFilename());
    	Path relativePath = DataStorageService.relativePath(account, project);
    	Path currLocation = this.rootLocation.resolve(relativePath);
    	Path destinationPath =  currLocation.resolve(filename);
    	
    	// 2. Create MetaData 
    	MetaData meta = metaDataService.getMetaData(project, destinationPath, 
    			DataSrcDstType.LOCAL_FS, "", DataStorageModel.DEFAULT_DELIMITER, DataStorageModel.DEFAULT_HASHEADER);
    	
    	// 3. Save file in separate thread
    	Future<?> saveDataFuture = executorService.submit(storeData(destinationPath, file)); // throws StorageException
    	
    	// 4. Get preview and persist it
    	DataPreview preview = metaDataService.persistPreview(meta, getPreview(meta, previewLinesCount));
    	
    	// 5. Generate DataAttributes and persist it
    	meta = metaDataService.generateAndPersistAttrubutes(meta, preview);
    	
    	try {
			saveDataFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			
			throw new StorageException("Exception in storage thread", e.getCause());
		}
    	
    	return meta;
    }
    
    @Override
    public List<String> getPreview(MetaData meta, int lineCount) {
    	List<String> preview = getPreview(Paths.get(meta.getStorage().getUri()), lineCount,
    			meta.getStorage().isHasHeader());
    	
    	return preview;
    }
    
    @Override
    public Stream<Path> loadAll(Path relativePath) throws StorageException {
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
    public Resource loadAsResource(Path relativePath, String filename)
    	throws StorageFileNotFoundException {
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
	public Path load(Path relativePath, String filename) {
		return rootLocation.resolve(relativePath).resolve(filename);
	}
	
	private void checkFile(MultipartFile file) throws UnsupportedFileTypeException, StorageException {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		Arrays.stream(EXTENSIONS).filter(ext -> StringUtils.getFilenameExtension(filename).toLowerCase().equals(ext))
				.findFirst().orElseThrow(
						() -> new UnsupportedFileTypeException(String.format("Unknown file extension %s", filename)));

		if (file.isEmpty()) {
			throw new StorageException("Failed to store empty file " + filename);
		}
		if (filename.contains("..")) {
			// This is a security check
			throw new StorageException("Cannot store file with relative path outside current directory " + filename);
		}
	}

	private List<String> getCsvPreview(Path path, int linesCount, boolean hasHeader) {
		int readLines = hasHeader ? linesCount + 1 : linesCount;
		List<String> lines;
		try (Stream<String> stream = Files.lines(path)) {
			lines = stream.limit(readLines).collect(Collectors.toCollection(ArrayList::new));

		} catch (IOException e) {
			throw new StorageFileNotFoundException(String.format("File %s", path.toString()), e);
		}

		return lines;
	}

	protected List<String> getPreview(Path path, int linesCount, boolean hasHeader) {

		String extension = StringUtils.getFilenameExtension(path.getFileName().toString()).toLowerCase();

		switch (extension) {
		case "csv":
			return getCsvPreview(path, linesCount, hasHeader);

		}

		throw new UnsupportedFileTypeException(String.format("Uncknown file extension .%s", extension));
	}
	

}
