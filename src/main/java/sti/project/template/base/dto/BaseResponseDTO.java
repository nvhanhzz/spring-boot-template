package sti.project.template.base.dto;

import lombok.Data;
import sti.project.template.base.entity.EntityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base response DTO with common fields.
 * All response DTOs should extend this class.
 */
@Data
public abstract class BaseResponseDTO {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EntityStatus status;
}
