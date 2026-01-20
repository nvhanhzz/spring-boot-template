package sti.project.template.base.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sti.project.template.base.entity.BaseEntity;

@Entity
@Table(name = "pending_files")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingFileEntity extends BaseEntity {

    @Column(name = "filename", length = 255, nullable = false, unique = true)
    String filename;

    @Column(name = "original_filename", length = 255, nullable = false)
    String originalFilename;

    @Column(name = "content_type", length = 100)
    String contentType;

    @Column(name = "size")
    Long size;
}