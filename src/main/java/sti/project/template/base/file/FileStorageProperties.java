package sti.project.template.base.file;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.file")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileStorageProperties {

    String uploadDir = "./uploads";
    long maxFileSize = 10 * 1024 * 1024;
    List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/gif", "image/webp");
    String baseUrl = "http://localhost";
    boolean cleanupEnabled = true;
    int cleanupAfterHours = 24;

    public String buildRelativePath(String filename) {
        if (filename == null || filename.isBlank())
            return null;
        String path = uploadDir.startsWith("/") ? uploadDir : "/" + uploadDir;
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        return path + "/" + filename;
    }

    public String buildFullUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank())
            return null;
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://"))
            return relativePath;
        return baseUrl + relativePath;
    }

    public String extractFilename(String relativePath) {
        if (relativePath == null || relativePath.isBlank())
            return null;
        int lastSlash = relativePath.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < relativePath.length() - 1) {
            return relativePath.substring(lastSlash + 1);
        }
        return relativePath;
    }
}