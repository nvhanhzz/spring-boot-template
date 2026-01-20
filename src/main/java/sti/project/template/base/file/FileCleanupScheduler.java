package sti.project.template.base.file;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileCleanupScheduler {

    FileStorageProperties properties;
    FileStorageService storageService;
    PendingFileRepository pendingFileRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOrphanFiles() {
        if (!properties.isCleanupEnabled()) {
            log.debug("File cleanup is disabled");
            return;
        }

        Instant threshold = Instant.now().minus(properties.getCleanupAfterHours(), ChronoUnit.HOURS);
        List<PendingFileEntity> orphanFiles = pendingFileRepository.findOlderThan(threshold);

        if (orphanFiles.isEmpty()) {
            log.debug("No orphan files to cleanup");
            return;
        }

        log.info("Found {} orphan files to cleanup", orphanFiles.size());
        int deletedCount = 0;

        for (PendingFileEntity pendingFile : orphanFiles) {
            try {
                storageService.delete(pendingFile.getFilename());
                pendingFileRepository.delete(pendingFile);
                deletedCount++;
            } catch (Exception e) {
                log.error("Failed to cleanup file: {}", pendingFile.getFilename(), e);
            }
        }

        log.info("Cleaned up {} orphan files", deletedCount);
    }
}