package sti.project.template.business.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

/**
 * Permission response DTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionResponse extends BaseResponseDTO {
    private String name;
    private String description;
}
