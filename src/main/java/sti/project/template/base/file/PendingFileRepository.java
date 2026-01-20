package sti.project.template.base.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PendingFileRepository extends JpaRepository<PendingFileEntity, UUID> {

    @Query("SELECT p FROM PendingFileEntity p WHERE p.createdAt < :threshold")
    List<PendingFileEntity> findOlderThan(Instant threshold);

    @Modifying
    @Query("DELETE FROM PendingFileEntity p WHERE p.filename = :filename")
    void deleteByFilename(String filename);
}