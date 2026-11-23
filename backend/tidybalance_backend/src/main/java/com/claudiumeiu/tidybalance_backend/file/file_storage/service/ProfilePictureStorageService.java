package com.claudiumeiu.tidybalance_backend.file.file_storage.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.claudiumeiu.tidybalance_backend.file.file_processing.service.ImageProcessingService;
import com.claudiumeiu.tidybalance_backend.file.file_storage.config.FileStorageProperties;
import com.claudiumeiu.tidybalance_backend.file.file_validation.model.FileTypeEnum;
import com.claudiumeiu.tidybalance_backend.file.file_validation.service.FileValidationService;

@Service
public class ProfilePictureStorageService {

    private final Path profilePictureRootLocation;
    private final FileValidationService fileValidationService;
    private final ImageProcessingService imageProcessingService;

    public ProfilePictureStorageService(FileStorageProperties fileStorageProperties,
            FileValidationService fileValidationService, ImageProcessingService imageProcessingService) {
        if (fileStorageProperties.getProfilePictureLocation().trim().isEmpty()) {
            throw new RuntimeException("Storage location can't be empty.");
        }

        this.profilePictureRootLocation = Paths.get(fileStorageProperties.getProfilePictureLocation());
        init();

        this.fileValidationService = fileValidationService;
        this.imageProcessingService = imageProcessingService;
    }

    public void init() {
        try {
            Files.createDirectories(profilePictureRootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't initialize the storage directory.", e);
        }
    }

    public void storeProfilePicture(MultipartFile file, String filename) {
        fileValidationService.validate(file, FileTypeEnum.IMAGE);

        Path destinationFile = profilePictureRootLocation
                .resolve(filename)
                .normalize()
                .toAbsolutePath();

        if (!destinationFile.getParent().equals(profilePictureRootLocation.toAbsolutePath())) {
            throw new RuntimeException("Cannot store the file outside current directory.");
        }

        imageProcessingService.validateDimensions(file);
        BufferedImage image = imageProcessingService.decode(file);
        imageProcessingService.encodeWebp(image, destinationFile);
    }

    public Resource loadProfilePicture(String filename) {
        try {
            Path filePath = this.profilePictureRootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error on file path: " + filename, e);
        }
    }

    public void deleteProfilePicture(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }

        try {
            Path filePath = this.profilePictureRootLocation.resolve(filename).normalize();
            if (Files.exists(filePath) && filePath.startsWith(this.profilePictureRootLocation)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            System.err.println("Could not delete profile picture: " + filename);
        }
    }
}