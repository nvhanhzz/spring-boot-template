package sti.project.template.base.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file);

    void delete(String filename);

    boolean exists(String filename);
}