package sti.project.template.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import sti.project.template.base.entity.EntityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseResponseDTO {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    EntityStatus status;
    AuditUserResponse createdByUser;
    AuditUserResponse updatedByUser;
}