package sti.project.template.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import sti.project.scada.base.entity.EntityStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseResponseDTO {
    UUID id;
    Instant createdAt;
    Instant updatedAt;
    EntityStatus status;
    AuditUserResponse createdByUser;
    AuditUserResponse updatedByUser;
}