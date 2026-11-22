package com.claudiumeiu.tidybalance_backend.file.file_validation.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.claudiumeiu.tidybalance_backend.file.file_validation.model.FileTypeEnum;

@Service
public class FileValidationService {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> IMAGE_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final Tika tika = new Tika();

    public void validate(MultipartFile file, FileTypeEnum fileTypeEnum) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file not allowed");
        }

        String filename = file.getOriginalFilename();

        validateFilename(filename, fileTypeEnum);
        validateFileContent(file, filename, fileTypeEnum);
    }

    public String extractNormalizedExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);

        if (extension == null) {
            throw new IllegalArgumentException("Missing extension");
        }

        return extension.toLowerCase(Locale.ROOT);
    }

    private void validateFileContent(MultipartFile file, String filename, FileTypeEnum fileTypeEnum) {
        String extension = extractNormalizedExtension(filename);
        String mediaType;

        try (InputStream inputStream = file.getInputStream()) {
            mediaType = tika.detect(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not detect file MIME type", e);
        }

        Set<String> allowedMimeTypes = switch (fileTypeEnum) {
            case IMAGE -> IMAGE_MIME_TYPES;
        };

        boolean extensionMatchesMimeType = switch (fileTypeEnum) {
            case IMAGE -> switch (extension) {
                case "jpg", "jpeg" -> mediaType.equals("image/jpeg");
                case "png" -> mediaType.equals("image/png");
                case "webp" -> mediaType.equals("image/webp");
                default -> false;
            };
        };

        if (!allowedMimeTypes.contains(mediaType)) {
            throw new IllegalArgumentException("MIME type not allowed");
        }

        if (!extensionMatchesMimeType) {
            throw new IllegalArgumentException("File extension does not match MIME type");
        }
    }

    private void validateFilename(String filename, FileTypeEnum fileTypeEnum) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Invalid filename");
        }

        if (filename.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Null byte detected");
        }

        if (filename.contains(":")) {
            throw new IllegalArgumentException("Colon not allowed");
        }

        Set<String> allowedExtensions = switch (fileTypeEnum) {
            case IMAGE -> IMAGE_EXTENSIONS;
        };

        String extension = extractNormalizedExtension(filename);

        if (!allowedExtensions.contains(extension)) {
            throw new IllegalArgumentException("Extension not allowed");
        }
    }
}
