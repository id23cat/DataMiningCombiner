package evm.dmc.web.service.impls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import evm.dmc.config.FileStorageConfig;
import evm.dmc.web.exceptions.StorageException;
import evm.dmc.web.exceptions.StorageFileNotFoundException;
import evm.dmc.web.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageConfig properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(Path relativePath, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path currLocation = this.rootLocation.resolve(relativePath);
        
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
     
            log.debug("Saving file: {}", currLocation.resolve(filename));
            Files.copy(file.getInputStream(), currLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
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

}
