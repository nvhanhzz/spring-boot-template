package sti.project.template.base.file;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocalFileStorageService implements FileStorageService {

    final FileStorageProperties properties;
    Path storagePath;

    @PostConstruct
    public void init() {
        try {
            storagePath = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);
            log.info("Initialized file storage at: {}", storagePath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.MEDIA_UPLOAD_FAILED, "Could not create upload directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String filename = generateUniqueFilename(originalFilename);

        try (InputStream inputStream = file.getInputStream()) {
            Path targetPath = storagePath.resolve(filename).normalize();
            if (!targetPath.startsWith(storagePath)) {
                throw new AppException(ErrorCode.INVALID_FILE, "Invalid file path");
            }
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Stored file: {} as {}", originalFilename, filename);
            return filename;
        } catch (IOException e) {
            throw new AppException(ErrorCode.MEDIA_UPLOAD_FAILED, "Failed to store file", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path filePath = storagePath.resolve(filename).normalize();
            if (!filePath.startsWith(storagePath)) {
                throw new AppException(ErrorCode.INVALID_FILE, "Invalid file path");
            }
            if (!Files.exists(filePath)) {
                throw new AppException(ErrorCode.MEDIA_NOT_FOUND, filename);
            }
            Files.delete(filePath);
            log.debug("Deleted file: {}", filename);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filename, e);
        }
    }

    @Override
    public boolean exists(String filename) {
        Path filePath = storagePath.resolve(filename).normalize();
        return filePath.startsWith(storagePath) && Files.exists(filePath);
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String baseName = originalFilename;
        if (!extension.isEmpty()) {
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        }
        String sanitized = baseName
                .toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        return sanitized + "-" + System.currentTimeMillis() + extension;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1)
            return "";
        return filename.substring(filename.lastIndexOf('.'));
    }
}