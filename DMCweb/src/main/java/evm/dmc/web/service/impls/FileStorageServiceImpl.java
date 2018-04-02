package evm.dmc.web.service.impls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
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
import evm.dmc.web.service.DataSetProperties;
import evm.dmc.web.service.DataStorageService;
import evm.dmc.web.service.MetaDataService;
import evm.dmc.web.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements DataStorageService {
//	@Autowired 
	FileStorageConfig properties;
	
//	@Autowired
	private ExecutorService executorService;
	
//	@Autowired
	private MetaDataService metaDataService;
	
//	@Autowired
	private ProjectService projectService;
	
    private Path rootLocation;
    
    private int previewLinesCount;
    
   
    private final static String[] EXTENSIONS = {"csv"};
    

    @Autowired
    public FileStorageServiceImpl(FileStorageConfig properties, MetaDataService metaDataService,
    		ExecutorService executorService, ProjectService projectService) {
    	this.properties = properties;
    	this.metaDataService = metaDataService;
    	this.executorService = executorService;
    	this.projectService = projectService;
    	
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
    
    @Override
    @Transactional
    public MetaData saveData(Account account, ProjectModel project, 
    		MultipartFile file, DataSetProperties datasetProperties) 
    		throws UnsupportedFileTypeException, StorageException {
    	// 1. Check file path, extension and non-empty data 
    	checkFileType(file);	// throws UnsupportedFileTypeException, StorageException
    	int requiredLinesCount = datasetProperties.isHasHeader() ? previewLinesCount+1 : previewLinesCount;
    	
    	String filename = StringUtils.cleanPath(file.getOriginalFilename());
    	Path relativePath = DataStorageService.relativePath(account, project);
    	Path currLocation = this.rootLocation.resolve(relativePath);
    	Path destinationPath =  currLocation.resolve(filename);
    	
    	// 2. Create MetaData
    	project = projectService.getOrSave(project);
    	MetaData meta = metaDataService.getMetaData(project, destinationPath, 
    			DataSrcDstType.LOCAL_FS, DataStorageModel.DEFAULT_DELIMITER,
    			datasetProperties);
    	
    	List<String> previewList = new ArrayList<>();
    	
    	// 3. Save file
    	Future<Path> saveDataFuture = saveFile(destinationPath, file, previewList, requiredLinesCount);
    	
    	log.debug("DEBUG: preview list: {}", previewList.toArray());
    	
    	try {
    		// 4. Get preview and persist it
    		DataPreview preview = metaDataService.createPreview(meta, previewList);
    		Map<MetaData, DataPreview> map = metaDataService.persistPreview(meta, preview);
    		meta = map.keySet().iterator().next();
    		preview = map.get(meta);

    		// 5. Generate DataAttributes and persist it
    		meta = metaDataService.generateAndPersistAttributes(meta, preview);
    	} catch(UnexpectedRollbackException ex) {
    		log.warn("Transavtion was rolled baack: ", ex.getCause());
    	}
    	
    	
    	try {
    		log.trace("Wait for get:{}", getTimeMs());
			saveDataFuture.get();
		} catch ( ExecutionException e) {
			throw new StorageException("Exception in storage thread", e.getCause());
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new StorageException("Waiting for finishing file download was interrupted", e);
		}

    	return meta;
    }
    
    @Override
    public DataPreview getPreview(MetaData meta) {
    	Optional<DataPreview> optPreview = metaDataService.getPreview(meta);
    	return optPreview.orElse(metaDataService.createPreview(meta, getPreview(meta, previewLinesCount)));
    }
    
    @Override
    public List<String> getPreview(MetaData meta, int lineCount) {
    	List<String> preview = getPreview(Paths.get(meta.getStorage().getUri()), lineCount,
    			meta.getStorage().isHasHeader());
    	
    	return preview;
    }
    
    
    @Override
    public Resource loadAsResource(MetaData metaData)
    	throws StorageFileNotFoundException {
    	Path file = path(metaData);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + file);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + file, e);
        }
    }

    @Override
    public void deleteAll(ProjectModel project) {
        FileSystemUtils.deleteRecursively(projectPath(project.getAccount(), project).toFile());
    }


	@Override
	public Path path(MetaData metaData) {
		return Paths.get(metaData.getStorage().getUri());
	}
	
	@Override
	public MetaData save(MetaData metaData) {
		return metaDataService.save(metaData);
	}
	
	/**
	 * @param destinationPath where to save file
	 * @param file data
	 * @param preview empty list after return have to contain preview lines
	 * @return future of thread that saves file to directory
	 */
	protected Future<Path> saveFile(Path destinationPath, MultipartFile file, List<String> preview, int linesCount) {
		BlockingQueue<List<String>> previewQueue = new ArrayBlockingQueue<>(1);
		BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>();
		
		Callable<Path> fileSaver = getFileSaver(destinationPath, dataQueue);
		Callable<Void> fileDownloader = getFileDownloader(file, dataQueue, previewQueue, linesCount);
		
		executorService.submit(fileDownloader);
		Future<Path> saver = executorService.submit(fileSaver);
		
		try {
			preview.addAll(previewQueue.take());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		return saver;
	}
	
	protected Callable<Path> getFileSaver(Path destinationPath, BlockingQueue<String> queue) {

		return new Callable<Path>() {
			private Path destinationPath;
			private BlockingQueue<String> queue;

			Callable<Path> init(Path destinationPath, BlockingQueue<String> queue) {
				this.destinationPath = destinationPath;
				this.queue = queue;
				return this;
			}

			@Override
			public Path call() throws StorageException {
				boolean interrupted = false;
				log.debug("Saving file: {}", destinationPath);
				log.trace("Start download:{}", getTimeMs());
				try {
					Files.createDirectories(destinationPath.getParent());
					Files.deleteIfExists(destinationPath);
				} catch (IOException e) {
					new StorageException("Can not create file: " + destinationPath, e);
				}
				try(BufferedWriter writer = Files.newBufferedWriter(destinationPath, StandardCharsets.UTF_8, 
						StandardOpenOption.CREATE,
						StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING)) {				
					while(true) {
						try {
							
							String line = queue.poll(properties.getFileWaitTimeoutMS(), TimeUnit.MILLISECONDS);
							if(line == null)
								break;		// file ends
							
							writer.write(line);
							writer.newLine();
							
						} catch (InterruptedException e) {
							interrupted = true;
						}
					}
				} catch (IOException e) {
					throw new StorageException("Failed to open file " + destinationPath, e);
				} finally {
					if(interrupted) {
						Thread.currentThread().interrupt();
					}
				}

				return destinationPath;

			}
		}.init(destinationPath, queue);
	}
	
	protected Callable<Void> getFileDownloader(MultipartFile file, 
			BlockingQueue<String> dataQueue, 
			BlockingQueue<List<String>> previewQueue, int linesCount) {
		
		return new Callable<Void>() {
			private MultipartFile file;
			private BlockingQueue<String> dataQueue;
			private BlockingQueue<List<String>> previewQueue;
			private int linesCount;
			
			public Callable<Void> init(MultipartFile file, 
					BlockingQueue<String> dataQueue, 
					BlockingQueue<List<String>> previewQueue, int linesCount) {
				this.file = file;
				this.dataQueue = dataQueue;
				this.previewQueue = previewQueue;
				this.linesCount = linesCount;
				return this;
			}
			
			@Override
			public Void call() throws StorageException {
				List<String> preview = new ArrayList<>(linesCount);
				int counter = linesCount;
				boolean getPreview = true;
				
				try(BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
					while(in.ready()) {
						String line = in.readLine();
						dataQueue.add(line);
						
						if(getPreview){
							if(counter > 0) {
								preview.add(line);
								counter--;
							} else {
								log.trace("TRACE: constructed preview list size: {}", preview.size());
								previewQueue.offer(preview);
								getPreview = false;
							}
						}
						
					}
					
					log.trace("TRACE: File has been downloaded correctly: {}", file.getName());
				} catch (IOException e) {
					throw new StorageException("Failed to download file " + file.getName(), e);
				}
				
				return null;
			}
			
		}.init(file, dataQueue, previewQueue, linesCount);
	}
	
	private void checkFileType(MultipartFile file) throws UnsupportedFileTypeException, StorageException {
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

	protected List<String> getPreview(Path path, int linesCount, boolean hasHeader) {
		log.debug("Getting preview from: {}", path);
		String extension = StringUtils.getFilenameExtension(path.getFileName().toString()).toLowerCase();

		switch (extension) {
		case "csv":
			return getCsvPreview(path, linesCount, hasHeader);

		}

		throw new UnsupportedFileTypeException(String.format("Uncknown file extension .%s", extension));
	}
	
	private List<String> getCsvPreview(Path path, int linesCount, boolean hasHeader) {
		int readLines = hasHeader ? linesCount + 1 : linesCount;
		List<String> lines;
		try (Stream<String> stream = Files.lines(path)) {
			lines = stream.limit(readLines).collect(Collectors.toCollection(ArrayList::new));

		} catch (IOException e) {
			throw new StorageFileNotFoundException(
					String.format("Attempt to read preview lines has failed: %s", path.toString()), e);
		}

		return lines;
	}

	private Path projectPath(Account account, ProjectModel project) {
		return this.rootLocation.resolve(DataStorageService.relativePath(account, project));
	}
	
	private long getTimeMs() {
    	return System.nanoTime();
    }

}
