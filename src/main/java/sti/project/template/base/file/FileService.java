package sti.project.template.base.file;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sti.project.scada.base.exception.AppException;
import sti.project.scada.base.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    FileStorageService storageService;
    FileStorageProperties properties;
    PendingFileRepository pendingFileRepository;

    @Transactional
    public FileUploadResponse upload(List<MultipartFile> files) {
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            validateFile(file);
            String filename = storageService.store(file);

            PendingFileEntity pendingFile = new PendingFileEntity();
            pendingFile.setFilename(filename);
            pendingFile.setOriginalFilename(file.getOriginalFilename());
            pendingFile.setContentType(file.getContentType());
            pendingFile.setSize(file.getSize());
            pendingFileRepository.save(pendingFile);

            log.info("Uploaded file: {} as {}", file.getOriginalFilename(), filename);
            paths.add(properties.buildRelativePath(filename));
        }
        return FileUploadResponse.builder()
                .baseUrl(properties.getBaseUrl())
                .paths(paths)
                .build();
    }

    @Transactional
    public void markAsUsed(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank())
            return;
        String filename = properties.extractFilename(fileUrl);
        if (filename == null)
            return;
        pendingFileRepository.deleteByFilename(filename);
        log.debug("Marked file as used: {}", filename);
    }

    @Transactional
    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty())
            return;

        List<String> filenames = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            String filename = properties.extractFilename(fileUrl);
            if (filename != null) {
                filenames.add(filename);
            }
        }

        for (String filename : filenames) {
            if (!storageService.exists(filename)) {
                throw new AppException(ErrorCode.MEDIA_NOT_FOUND, filename);
            }
        }

        for (String filename : filenames) {
            storageService.delete(filename);
            pendingFileRepository.deleteByFilename(filename);
            log.info("Deleted file: {}", filename);
        }
    }

    void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE, "File is empty");
        }
        if (file.getSize() > properties.getMaxFileSize()) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE, "Max size: " + properties.getMaxFileSize() + " bytes");
        }
        String contentType = file.getContentType();
        if (contentType == null || !properties.getAllowedTypes().contains(contentType)) {
            throw new AppException(ErrorCode.UNSUPPORTED_MEDIA_FORMAT, "Allowed: " + properties.getAllowedTypes());
        }
    }
}