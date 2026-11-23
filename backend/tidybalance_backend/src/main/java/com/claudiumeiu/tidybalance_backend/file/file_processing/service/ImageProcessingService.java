package com.claudiumeiu.tidybalance_backend.file.file_processing.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageProcessingService {

    private static final int MAX_IMAGE_WIDTH = 2048;
    private static final int MAX_IMAGE_HEIGHT = 2048;
    private static final long MAX_IMAGE_PIXELS = 4_000_000L;

    public void validateDimensions(MultipartFile file) {
        try (ImageInputStream input = ImageIO.createImageInputStream(file.getInputStream())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Unsupported image format");
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(input);

                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width > MAX_IMAGE_WIDTH
                        || height > MAX_IMAGE_HEIGHT
                        || (long) width * height > MAX_IMAGE_PIXELS) {
                    throw new IllegalArgumentException("Image dimensions are too large");
                }
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not validate image dimensions", e);
        }
    }

    public BufferedImage decode(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            if (image == null) {
                throw new IllegalArgumentException("Invalid or unsupported image");
            }

            return image;
        } catch (IOException e) {
            throw new RuntimeException("Could not decode image", e);
        }
    }

    public void encodeWebp(BufferedImage image, Path destinationPath) {
        try (OutputStream outputStream = Files.newOutputStream(destinationPath)) {
            boolean encoded = ImageIO.write(image, "webp", outputStream);

            if (!encoded) {
                throw new IOException("No WebP writer available");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not encode image as WebP", e);
        }
    }
}
