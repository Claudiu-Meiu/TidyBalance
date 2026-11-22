package com.claudiumeiu.tidybalance_backend.file.file_processing.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageProcessingService {

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
